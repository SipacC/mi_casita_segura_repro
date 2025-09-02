package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import Config.Conexion;  
import java.io.IOException;
import java.sql.*;

import websocket.ArduinoSerial;

@ServerEndpoint("/qrSocket")
public class QrSocket {

    private static String ultimoQR = "";
    private static long tiempoUltimoQR = 0;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Conexión WebSocket abierta: " + session.getId());
        ArduinoSerial.conectar("COM3", 115200);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("QR recibido: " + message);
        String respuesta = validarQr(message);
        session.getBasicRemote().sendText(respuesta);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Conexión WebSocket cerrada: " + session.getId());
        ArduinoSerial.cerrar();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private String validarQr(String message) {
        long ahora = System.currentTimeMillis();
        if (message.equals(ultimoQR) && (ahora - tiempoUltimoQR < 2000)) {
            System.out.println("Ignorado QR duplicado en servidor: " + message);
            return "{\"resultado\":\"duplicado\"}";
        }
        ultimoQR = message;
        tiempoUltimoQR = ahora;

        Conexion cn = new Conexion();
        try (Connection con = cn.getConnection()) {

            String[] partes = message.split(";");
            String tipo = "entrada";
            String idStr;
            String codigo;

            if (message.startsWith("tipo:")) {
                tipo = partes[0].split(":")[1];  
                idStr = partes[1].split(":")[1];
                codigo = partes[2].split(":")[1];
            } else {
                idStr = partes[0].split(":")[1];
                codigo = partes[1].split(":")[1];
            }

            int idUsuario = Integer.parseInt(idStr);

            PreparedStatement ps = con.prepareStatement(
                "SELECT q.id_qr_usuario, u.nombres " +
                "FROM qr_usuario q " +
                "INNER JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                "WHERE q.id_usuario=? AND q.codigo_qr_usuario=? AND q.estado='activo'"
            );
            ps.setInt(1, idUsuario);
            ps.setString(2, codigo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int idQrUsuario = rs.getInt("id_qr_usuario");
                String usuario = rs.getString("nombres");

                if ("entrada".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("1"); // ✅ ahora manda "1"
                } else if ("salida".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("2"); // ✅ ahora manda "2"
                }

                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO acceso_usuario (id_qr_usuario, tipo) VALUES (?, ?)"
                );
                ps2.setInt(1, idQrUsuario);
                ps2.setString(2, tipo);
                ps2.executeUpdate();

                return "{\"resultado\":\"valido\",\"usuario\":\"" + usuario + "\"}";
            } else {
                return "{\"resultado\":\"invalido\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"resultado\":\"error\"}";
        }
    }
}
