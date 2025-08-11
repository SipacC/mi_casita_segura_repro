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
        String sql = "SELECT id, dpi, nombres, rol, contrasena FROM persona";
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
                per.setContrasena(rs.getString("contrasena"));
                list.add(per);
            }
        } catch (Exception e) {
            System.err.println("Error en listar(): " + e.getMessage());
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
                per.setContrasena(rs.getString("contrasena"));
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
            ps.setString(4, per.getContrasena());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error en add(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean edit(Persona per) {
        try {
            con = cn.getConnection();

            //sino se ingresa nueva contrasena
            if (per.getContrasena() == null || per.getContrasena().trim().isEmpty()) {
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setInt(4, per.getId());
            } else {
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ?, contrasena = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setString(4, per.getContrasena());
                ps.setInt(5, per.getId());
            }
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error en edit(): " + e.getMessage());
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
            System.err.println("Error en eliminar(): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Persona findByCredenciales(String nom, String contrasena, String rol) {
        String sql = "SELECT id, dpi, nombres, rol FROM persona WHERE nombres = ? AND contrasena = ? AND LOWER(rol) = LOWER(?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);
            ps.setString(2, contrasena);
            ps.setString(3, rol);
            rs = ps.executeQuery();
            if (rs.next()){
                Persona per = new Persona();
                per.setId(rs.getInt("id"));
                per.setDpi(rs.getString("dpi"));
                per.setNom(rs.getString("nombres"));
                per.setRol(rs.getString("rol"));
                return per;
            }
        } catch (Exception e) {
            System.err.println("Error en buscar por credenciales: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return null;
    }
}
