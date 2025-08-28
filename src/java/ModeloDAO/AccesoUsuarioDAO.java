package ModeloDAO;

import Config.Conexion;
import Modelo.AccesoUsuario;
import java.sql.*;

public class AccesoUsuarioDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;

    // Insertar un acceso de usuario (ej. cuando usa el QR)
    public boolean add(AccesoUsuario acceso) {
        String sql = "INSERT INTO acceso_usuario(id_qr_usuario, tipo, resultado, estado) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, acceso.getId_qr_usuario());
            ps.setString(2, acceso.getTipo());
            ps.setString(3, acceso.getResultado());
            ps.setString(4, acceso.getEstado());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error en AccesoUsuarioDAO.add(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
