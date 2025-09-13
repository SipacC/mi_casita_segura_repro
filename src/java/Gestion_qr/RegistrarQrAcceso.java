package Gestion_qr;

import Config.Conexion;
import java.sql.*;

public class RegistrarQrAcceso {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    /**
     * Inserta en qr_usuario y acceso_usuario al mismo tiempo
     * @param idUsuario -> id del usuario creado
     * @param codigoQR -> el código aleatorio generado para el QR
     * @param rol -> rol del usuario creado (residente, administrador, seguridad)
     * @return id_qr_usuario generado
     */
    public int registrarQrYAcceso(int idUsuario, String codigoQR, String rol) {
        int idQrUsuario = -1;
        try {
            con = cn.getConnection();

            String sqlQr = "INSERT INTO qr_usuario(codigo_qr_usuario, id_usuario, tipo, estado) VALUES (?, ?, ?, ?)";
            ps = con.prepareStatement(sqlQr, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, codigoQR);
            ps.setInt(2, idUsuario);
            ps.setString(3, rol);       
            ps.setString(4, "activo");
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idQrUsuario = rs.getInt(1);
            }

            if (idQrUsuario > 0) {
                String sqlAcceso = "INSERT INTO acceso_usuario(id_qr_usuario, tipo) VALUES (?, ?)";
                ps = con.prepareStatement(sqlAcceso);
                ps.setInt(1, idQrUsuario);
                ps.setString(2, rol);
                ps.executeUpdate();
            }

            System.out.println("Registro en qr_usuario y acceso_usuario completado (id_qr_usuario=" + idQrUsuario + ")");
        } catch (Exception e) {
            System.err.println("Error en registrarQrYAcceso: " + e.getMessage());
            e.printStackTrace();
        }
        return idQrUsuario;
    }
}
