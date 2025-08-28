package ModeloDAO;

import Config.Conexion;
import Modelo.Notificacion;
import java.sql.*;

public class NotificacionDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;

    // Insertar notificación
    public boolean add(Notificacion notif) {
        String sql = "INSERT INTO Notificaciones(id_usuario, asunto, mensaje, tipo_evento) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, notif.getId_usuario());
            ps.setString(2, notif.getAsunto());
            ps.setString(3, notif.getMensaje());
            ps.setString(4, notif.getTipo_evento());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error en NotificacionDAO.add(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
