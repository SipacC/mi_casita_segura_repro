package ModeloDAO;

import Config.Conexion;
import Intefaces.CRUD;
import Modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements CRUD {// la clase Persona implementa los metodos de la interfaz crud
    Conexion cn = new Conexion(); // objeto para manejar la base de datos, instancia a la clase conexion
    //variables JDBC reutilizables para las conexiones, consultas
    Connection con; //representa la conexion a la base de datos
    PreparedStatement ps; // representa una consulta AQL precompilada
    ResultSet rs;//contiene el resultado de una consulta

    @Override
    public List<Persona> listar() {
        // Lista que almacenará todos los registros de la tabla persona
        List<Persona> list = new ArrayList<>();
        // Consulta SQL para obtener todos los campos de la tabla persona
        String sql = "SELECT id, dpi, nombres, rol, contrasena FROM persona";
        try {
            con = cn.getConnection();//habre la conexion a la base de datos
            ps = con.prepareStatement(sql);//prepara la consulta sql
            rs = ps.executeQuery();// ejecuta la consulta y obtiene los resultados, los datos
            while (rs.next()) {//recorre todos los resultados
                Persona per = new Persona();
                per.setId(rs.getInt("id"));
                per.setDpi(rs.getString("dpi"));
                per.setNom(rs.getString("nombres"));
                per.setRol(rs.getString("rol"));
                per.setContrasena(rs.getString("contrasena"));
                list.add(per);
            }
        } catch (Exception e) {
            //imprime en consola si hay algun problema
            System.err.println("Error en listar(): " + e.getMessage());
            e.printStackTrace();
        }
        return list;//devuelve la lista completa de personas
    }

    @Override
    public Persona list(int id) {
        String sql = "SELECT id, dpi, nombres, rol, contrasena FROM persona WHERE id = ?";
        Persona per = null; // Objeto Persona que devolverá el método
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);// Asignar parámetro ID
            rs = ps.executeQuery();// Ejecutar consult

            if (rs.next()) {// Si hay resultado, crear objeto Persona y asignar valores
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
        return per;// Devolver el objeto Persona encontrado o null si no existe

    }

    @Override
    public boolean add(Persona per) {
        //consulta sql para un nuevo registro
        String sql = "INSERT INTO persona(dpi, nombres, rol, contrasena) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection(); //abre la conexion
            ps = con.prepareStatement(sql);// prepara la consulta
            ps.setString(1, per.getDpi());//asigna el dpi
            ps.setString(2, per.getNom());//asigna el nombre
            ps.setString(3, per.getRol());//asigna el rol
            ps.setString(4, per.getContrasena());// asigna la contrasena
            // Ejecutar la inserción, devuelve true si se insertó al menos 1 fila
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("Error en add(): " + e.getMessage());
            e.printStackTrace();
            return false;// Devuelve false si ocurre algún error
        }
    }

    @Override
    public boolean edit(Persona per) {
        try {
            con = cn.getConnection();// Abrir conexión

            // Si la contraseña está vacía, no actualizarla
            if (per.getContrasena() == null || per.getContrasena().trim().isEmpty()) {
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setInt(4, per.getId());
            } else {
                // Si la contraseña se proporciona, actualizar también ese campo
                String sql = "UPDATE persona SET dpi = ?, nombres = ?, rol = ?, contrasena = ? WHERE id = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, per.getDpi());
                ps.setString(2, per.getNom());
                ps.setString(3, per.getRol());
                ps.setString(4, per.getContrasena());
                ps.setInt(5, per.getId());
            }
            return ps.executeUpdate() > 0;// Retorna true si la actualización tuvo efecto
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
    public Persona findByLogin(String nom, String contrasena) {
        String sql = "SELECT id, dpi, nombres, rol FROM persona WHERE nombres = ? AND contrasena = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nom);// Asignar nombre de usuario
            ps.setString(2, contrasena);// Asignar contraseña
            rs = ps.executeQuery();// Asignar contraseña

            if (rs.next()) {// Si se encuentra usuario
                Persona per = new Persona();
                per.setId(rs.getInt("id"));// Asignar ID
                per.setDpi(rs.getString("dpi"));// Asignar dpi
                per.setNom(rs.getString("nombres"));// Asignar nombre
                per.setRol(rs.getString("rol"));// Asignar rol
                return per;// Devolver usuario encontrado
            }
        } catch (Exception e) {
            System.err.println("Error en findByLogin: " + e.getMessage());
            e.printStackTrace();
        }
        return null;// Retorna null si no se encuentra usuario
    }


}
