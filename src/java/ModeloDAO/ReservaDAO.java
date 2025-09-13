package ModeloDAO;

import Config.Conexion;
import Modelo.Reserva;
import java.sql.*;
import java.util.*;

public class ReservaDAO {
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    public List<Reserva> listarPorUsuario(int idUsuario) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id_reserva, r.id_usuario, r.id_area, r.fecha_reserva, " +
                     "r.hora_inicio, r.hora_fin, r.estado, a.nombre AS nombre_area " +
                     "FROM reserva r " +
                     "JOIN areacomun a ON r.id_area = a.id_area " +
                     "WHERE r.id_usuario = ? " +
                     "ORDER BY r.fecha_reserva DESC, r.hora_inicio";
        try {
            con = new Conexion().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId_reserva(rs.getInt("id_reserva"));
                r.setId_usuario(rs.getInt("id_usuario"));
                r.setId_area(rs.getInt("id_area"));
                r.setNombreArea(rs.getString("nombre_area"));
                r.setFecha_reserva(rs.getString("fecha_reserva"));
                r.setHora_inicio(rs.getString("hora_inicio"));
                r.setHora_fin(rs.getString("hora_fin"));
                r.setEstado(rs.getString("estado"));
                lista.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error listarPorUsuario: " + e.getMessage());
        }
        return lista;
    }
    public List<Reserva> buscarPorNombre(int idUsuario, String nombre) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT r.id_reserva, r.id_usuario, r.id_area, r.fecha_reserva, " +
                     "r.hora_inicio, r.hora_fin, r.estado, a.nombre AS nombre_area " +
                     "FROM reserva r " +
                     "JOIN areacomun a ON r.id_area = a.id_area " +
                     "WHERE r.id_usuario = ? AND a.nombre ILIKE ? " +
                     "ORDER BY r.fecha_reserva DESC, r.hora_inicio";
        try {
            con = new Conexion().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setString(2, "%" + nombre + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Reserva r = new Reserva();
                r.setId_reserva(rs.getInt("id_reserva"));
                r.setId_usuario(rs.getInt("id_usuario"));
                r.setId_area(rs.getInt("id_area"));
                r.setNombreArea(rs.getString("nombre_area"));
                r.setFecha_reserva(rs.getString("fecha_reserva"));
                r.setHora_inicio(rs.getString("hora_inicio"));
                r.setHora_fin(rs.getString("hora_fin"));
                r.setEstado(rs.getString("estado"));
                lista.add(r);
            }
        } catch (Exception e) {
            System.out.println("Error buscarPorNombre: " + e.getMessage());
        }
        return lista;
    }

    
    public int insertar(Reserva r) {
        String sql = "INSERT INTO reserva (id_usuario, id_area, fecha_reserva, hora_inicio, hora_fin, estado) " +
                     "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_reserva";
        try {
            con = new Conexion().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getId_usuario());
            ps.setInt(2, r.getId_area());
            ps.setDate(3, java.sql.Date.valueOf(r.getFecha_reserva())); 
            ps.setTime(4, java.sql.Time.valueOf(r.getHora_inicio() + ":00"));
            ps.setTime(5, java.sql.Time.valueOf(r.getHora_fin() + ":00"));
            ps.setString(6, r.getEstado());

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error insertar: " + e.getMessage());
        }
        return -1;
    }

    
    public boolean validarDisponibilidad(Reserva r) { // si una area esta dispoible
        String sql = "SELECT COUNT(*) FROM reserva " +
                     "WHERE id_area = ? AND fecha_reserva = ? " +
                     "AND estado = 'Activa' " +
                     "AND ((hora_inicio < ? AND hora_fin > ?) OR " +
                     "     (hora_inicio < ? AND hora_fin > ?))";
        try {
            con = new Conexion().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, r.getId_area());
            ps.setDate(2, java.sql.Date.valueOf(r.getFecha_reserva()));
            ps.setTime(3, java.sql.Time.valueOf(r.getHora_fin() + ":00"));
            ps.setTime(4, java.sql.Time.valueOf(r.getHora_inicio() + ":00"));
            ps.setTime(5, java.sql.Time.valueOf(r.getHora_inicio() + ":00"));
            ps.setTime(6, java.sql.Time.valueOf(r.getHora_fin() + ":00"));

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0;
            }
        } catch (Exception e) {
            System.out.println("Error validarDisponibilidad: " + e.getMessage());
        }
        return false;
    }

    public boolean cancelar(int idReserva) {
        String sql = "UPDATE reserva SET estado = 'Cancelada' WHERE id_reserva = ?";
        try {
            con = new Conexion().getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idReserva);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println("Error cancelar: " + e.getMessage());
        }
        return false;
    }
}
