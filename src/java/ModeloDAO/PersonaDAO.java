package ModeloDAO;

import Config.Conexion;
import Intefaces.CRUD;
import Modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements CRUD {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public List<Persona> listar() {
        List<Persona> list = new ArrayList<>();
        String sql = "SELECT id, dpi, nombres, rol FROM persona";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Persona per = new Persona();
                per.setId(rs.getInt("id"));
                per.setDpi(rs.getString("dpi"));
                per.setNom(rs.getString("nombres"));
                per.setRol(rs.getString("rol"));
                // Por seguridad, no cargamos contrasena en listados
                list.add(per);
            }
        } catch (Exception e) {
            System.err.println("❌ Error en listar(): " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Persona list(int id) {
        String sql = "SELECT id, dpi, nombres, rol, contrasena FROM persona WHERE id = ?";
        Persona per = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                per = new Persona();
                per.setId(rs.getInt("id"));
                per.setDpi(rs.getString("dpi"));
                per.setNom(rs.getString("nombres"));
                per.setRol(rs.getString("rol"));
                per.setContra(rs.getString("contrasena"));
            }
        } catch (Exception e) {
            System.err.println(" Error en list(id): " + e.getMessage());
            e.printStackTrace();
        }
        return per;
    }

    @Override
    public boolean add(Persona per) {
        String sql = "INSERT INTO persona(dpi, nombres, rol, contrasena) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, per.getDpi());
            ps.setString(2, per.getNom());
            ps.setString(3, per.getRol());
            ps.setString(4, per.getContra()); // en prod: guarda hash
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error en add(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean edit(Persona per) {
        try {
            con = cn.getConnection();

            // Si viene nueva contraseña, se actualiza; si no, se deja igual
            if (per.getContra() != null && !per.getContra().trim().isEmpty()) {
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ?, contrasena = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setString(4, per.getContra());
                ps.setInt(5, per.getId());
            } else {
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setInt(4, per.getId());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error en edit(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM persona WHERE id = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error en eliminar(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ---- Login (extra) ----
    // Nota: no uso @Override porque tu interfaz CRUD puede que no tenga este método.
    public Persona login(String dpi, String contra) {
        String sql = "SELECT id, dpi, nombres, rol, contrasena FROM persona WHERE dpi = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, dpi);
            rs = ps.executeQuery();
            if (rs.next()) {
                String stored = rs.getString("contrasena");
                // En producción: usar BCrypt.checkpw(contra, stored)
                if (contra != null && contra.equals(stored)) {
                    Persona u = new Persona();
                    u.setId(rs.getInt("id"));
                    u.setDpi(rs.getString("dpi"));
                    u.setNom(rs.getString("nombres"));
                    u.setRol(rs.getString("rol"));
                    return u;
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error en login(): " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
