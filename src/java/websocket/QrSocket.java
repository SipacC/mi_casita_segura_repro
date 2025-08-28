package websocket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import Config.Conexion;

import java.io.IOException;
import java.sql.*;

// Endpoint WebSocket disponible en ws://IP:PUERTO/CRUD-MVC-JAVA/qrSocket
@ServerEndpoint("/qrSocket")
public class QrSocket {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("✅ Conexión WebSocket abierta: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("📩 QR recibido: " + message);

        // 👉 Validar QR en la BD
        String respuesta = validarQr(message);

        // 👉 Responder al cliente en JSON
        System.out.println("📤 Respuesta enviada al cliente: " + respuesta);
        session.getBasicRemote().sendText(respuesta);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("❌ Conexión WebSocket cerrada: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    // ===============================================================
    // 📌 MÉTODO DE VALIDACIÓN DEL QR
    // ===============================================================
    private String validarQr(String message) {
        Conexion cn = new Conexion();
        try (Connection con = cn.getConnection()) {

            // Puede venir como:
            // "tipo:entrada;ID:9;CODIGO:xxxx"
            // o "ID:9;CODIGO:xxxx"

            String tipo = "entrada"; // valor por defecto
            int idUsuario;
            String codigo;

            String[] partes = message.split(";");

            if (message.startsWith("tipo:")) {
                // Formato largo con tipo
                tipo = partes[0].split(":")[1];                   // entrada o salida
                idUsuario = Integer.parseInt(partes[1].split(":")[1]); // ID usuario
                codigo = partes[2].split(":")[1];                 // código QR
            } else {
                // Formato corto sin tipo
                idUsuario = Integer.parseInt(partes[0].split(":")[1]);
                codigo = partes[1].split(":")[1];
            }

            // 🟢 Validación en BD
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

                // Registrar acceso en la bitácora
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO acceso_usuario (id_qr_usuario, tipo, resultado, estado) " +
                    "VALUES (?, ?, 'usado', 'activo')"
                );
                ps2.setInt(1, idQrUsuario);
                ps2.setString(2, tipo);
                ps2.executeUpdate();

                // ✅ Respuesta al cliente
                return "{\"resultado\":\"valido\",\"usuario\":\"" + usuario + "\"}";
            } else {
                return "{\"resultado\":\"invalido\"}";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"resultado\":\"error\",\"detalle\":\"" + e.getMessage() + "\"}";
        }
    }
}
