package ModeloDAO;

import Config.Conexion;
import Modelo.QrUsuario;
import java.sql.*;

public class QrUsuarioDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Insertar un QR y devolver su ID
    public int add(QrUsuario qr) {
        String sql = "INSERT INTO qr_usuario(codigo_qr_usuario, id_usuario, tipo, estado) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, qr.getCodigo_qr_usuario());
            ps.setInt(2, qr.getId_usuario());
            ps.setString(3, qr.getTipo());
            ps.setString(4, qr.getEstado());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // id_qr_usuario generado
                }
            }
        } catch (Exception e) {
            System.err.println("Error en QrUsuarioDAO.add(): " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
}
