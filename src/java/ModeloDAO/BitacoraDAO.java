package ModeloDAO;

import Modelo.Bitacora;
import Config.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {
    public void registrarAccion(int id_usuario, String accion, String modulo) {
        String sql = "INSERT INTO bitacora (id_usuario, accion, modulo) VALUES (?, ?, ?)";

        Conexion conexion = new Conexion();
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);
            ps.setString(2, accion);
            ps.setString(3, modulo);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<Bitacora> listar(Integer idUsuario, String modulo) {
        List<Bitacora> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
            "SELECT b.id_bitacora, " +
            "       b.id_usuario, " +
            "       u_actor.nombres AS usuario_actor, " +
            "       u_afectado.nombres AS usuario_afectado, " +
            "       b.accion, b.modulo, b.fecha_hora " +
            "FROM bitacora b " +
            "JOIN usuarios u_actor ON b.id_usuario = u_actor.id_usuario " +
            "LEFT JOIN usuarios u_afectado " +
            "       ON u_afectado.id_usuario = CASE " +
            "           WHEN regexp_replace(b.accion, '[^0-9]', '', 'g') ~ '^[0-9]+$' " +
            "           THEN CAST(regexp_replace(b.accion, '[^0-9]', '', 'g') AS INT) " +
            "           ELSE NULL END " +
            "WHERE 1=1 "
        );

        if (idUsuario != null) {
            sql.append("AND b.id_usuario = ? ");
        }
        if (modulo != null && !modulo.trim().isEmpty()) {
            sql.append("AND b.modulo = ? ");
        }

        sql.append("ORDER BY b.fecha_hora DESC");

        Conexion conexion = new Conexion();
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (idUsuario != null) {
                ps.setInt(index++, idUsuario);
            }
            if (modulo != null && !modulo.trim().isEmpty()) {
                ps.setString(index++, modulo);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_usuario(rs.getInt("id_usuario"));
                b.setUsuario_actor(rs.getString("usuario_actor"));
                b.setUsuario_afectado(rs.getString("usuario_afectado"));
                b.setAccion(rs.getString("accion"));
                b.setModulo(rs.getString("modulo"));
                b.setFecha_hora(rs.getTimestamp("fecha_hora"));
                lista.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}
