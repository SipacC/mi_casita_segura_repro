package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import Config.Conexion;
import java.io.IOException;
import java.sql.*;

@ServerEndpoint("/qrSocket")
public class QrSocket {

    private static String ultimoQR = "";
    private static long tiempoUltimoQR = 0;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("✅ Conexión WebSocket abierta: " + session.getId());
        try {
            ArduinoSerial.conectar("COM3", 115200);
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con Arduino: " + e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("📩 QR recibido: " + message);
        String respuesta = validarQr(message);
        session.getBasicRemote().sendText(respuesta);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("🔌 Conexión WebSocket cerrada: " + session.getId());
        try {
            ArduinoSerial.cerrar();
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar conexión con Arduino: " + e.getMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("💥 Error en WebSocket: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    private String validarQr(String message) {
        long ahora = System.currentTimeMillis();
        if (message.equals(ultimoQR) && (ahora - tiempoUltimoQR < 2000)) {
            System.out.println("⚠️ Ignorado QR duplicado en servidor (anti-spam): " + message);
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

                // Consultar último acceso
                PreparedStatement psUltimo = con.prepareStatement(
                    "SELECT tipo FROM acceso_usuario WHERE id_qr_usuario=? ORDER BY fecha_hora DESC LIMIT 1"
                );
                psUltimo.setInt(1, idQrUsuario);
                ResultSet rsUltimo = psUltimo.executeQuery();

                if (rsUltimo.next()) {
                    String ultimoTipo = rsUltimo.getString("tipo");

                    if ("entrada".equalsIgnoreCase(tipo) && "entrada".equalsIgnoreCase(ultimoTipo)) {
                        System.out.println("⚠️ Usuario ya estaba dentro → no puede volver a entrar.");
                        return "{\"resultado\":\"duplicado\",\"usuario\":\"" + usuario + "\"}";
                    }

                    if ("salida".equalsIgnoreCase(tipo) && "salida".equalsIgnoreCase(ultimoTipo)) {
                        System.out.println("⚠️ Usuario ya estaba fuera → no puede volver a salir.");
                        return "{\"resultado\":\"duplicado\",\"usuario\":\"" + usuario + "\"}";
                    }
                }

                // Registrar acceso válido
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO acceso_usuario (id_qr_usuario, tipo, fecha_hora) VALUES (?, ?, NOW())"
                );
                ps2.setInt(1, idQrUsuario);
                ps2.setString(2, tipo);
                ps2.executeUpdate();

                // Enviar señal al Arduino
                if ("entrada".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("1");
                } else if ("salida".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("2");
                }

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

