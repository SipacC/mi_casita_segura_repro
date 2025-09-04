package ModeloDAO;

import Modelo.Bitacora;
import Config.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BitacoraDAO {

    public void registrarAccion(int id_usuario, String accion, String modulo) {
        String sql = "INSERT INTO bitacora (id_usuario, accion, modulo) VALUES (?, ?, ?)";

        Conexion conexion = new Conexion(); // ✅ CORRECTO
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

    public List<Bitacora> listar() {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM bitacora ORDER BY fecha_hora DESC";

        Conexion conexion = new Conexion(); // ✅ CORRECTO
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_usuario(rs.getInt("id_usuario"));
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

    public List<Bitacora> listarPorUsuario(int id_usuario) {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM bitacora WHERE id_usuario = ? ORDER BY fecha_hora DESC";

        Conexion conexion = new Conexion(); // ✅ CORRECTO
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id_usuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_usuario(rs.getInt("id_usuario"));
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

    public List<Bitacora> listarPorModulo(String modulo) {
        List<Bitacora> lista = new ArrayList<>();
        String sql = "SELECT * FROM bitacora WHERE modulo = ? ORDER BY fecha_hora DESC";

        Conexion conexion = new Conexion(); // ✅ CORRECTO
        try (Connection con = conexion.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, modulo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Bitacora b = new Bitacora();
                b.setId_bitacora(rs.getInt("id_bitacora"));
                b.setId_usuario(rs.getInt("id_usuario"));
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
