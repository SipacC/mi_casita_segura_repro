package ModeloDAO;

import Config.Conexion;
import Intefaces.CRUD;
import Modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PersonaDAO implements CRUD {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    @Override
    public List<Persona> listar() {
        List<Persona> list = new ArrayList<>();
        String sql = "SELECT id_usuario, dpi, nombres, usuario, rol, contrasena, correo, lote, numero_casa, estado FROM usuarios";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Persona per = new Persona();
                per.setId_usuario(rs.getInt("id_usuario"));
                per.setDpi(rs.getString("dpi"));
                per.setNombres(rs.getString("nombres"));
                per.setUsuario(rs.getString("usuario"));
                per.setRol(rs.getString("rol"));
                per.setContrasena(rs.getString("contrasena"));
                per.setCorreo(rs.getString("correo"));
                per.setLote(rs.getString("lote"));
                per.setNumero_casa(rs.getString("numero_casa"));
                per.setEstado(rs.getString("estado"));
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
        String sql = "SELECT id_usuario, dpi, nombres, usuario, rol, contrasena, correo, lote, numero_casa, estado FROM usuarios WHERE id_usuario = ?";
        Persona per = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                per = new Persona();
                per.setId_usuario(rs.getInt("id_usuario"));
                per.setDpi(rs.getString("dpi"));
                per.setNombres(rs.getString("nombres"));
                per.setUsuario(rs.getString("usuario"));
                per.setRol(rs.getString("rol"));
                per.setContrasena(rs.getString("contrasena"));
                per.setCorreo(rs.getString("correo"));
                per.setLote(rs.getString("lote"));
                per.setNumero_casa(rs.getString("numero_casa"));
                per.setEstado(rs.getString("estado"));
            }
        } catch (Exception e) {
            System.err.println("Error en list(id): " + e.getMessage());
            e.printStackTrace();
        }
        return per;
    }

    @Override
    public int add(Persona per) {
        String sql = "INSERT INTO usuarios(dpi, nombres, usuario, rol, contrasena, correo, lote, numero_casa, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, per.getDpi());
            ps.setString(2, per.getNombres());
            ps.setString(3, per.getUsuario());
            ps.setString(4, per.getRol());
            ps.setString(5, per.getContrasena());
            ps.setString(6, per.getCorreo());
            ps.setString(7, per.getLote());
            ps.setString(8, per.getNumero_casa());
            ps.setString(9, per.getEstado());

            int rows = ps.executeUpdate();

            if (rows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int idUsuario =rs.getInt(1);
                    //genera el codigo eleatorio
                    String codigoQR = Gestion_qr.CrearCodigo.generarCodigo();
                    //Generar el qr en disco
                    String rutaQR = Gestion_qr.CrearQr.generarQR(idUsuario, codigoQR);
                    //inserta en qr_usuario y acceso_usuario
                    Gestion_qr.RegistrarQrAcceso registrar = new Gestion_qr.RegistrarQrAcceso();
                    int idQrUsuario = registrar.registrarQrYAcceso(idUsuario, codigoQR, per.getRol());
                    //enviar correo al usuario + qr solo si todo salio bien
                    if (idQrUsuario > 0){
                        Gestion_qr.correoEnviarQr correo = new Gestion_qr.correoEnviarQr();
                        correo.enviarConQR(
                        per.getCorreo(),
                        per.getNombres(),
                        rutaQR);
                    }
                    
                    

                    System.out.println("✅ Usuario creado con ID " + idUsuario);
                    System.out.println("✅ QR generado en: " + rutaQR);
                    return idUsuario;
                }                
            }
        } catch (Exception e) {
            System.err.println("Error en add():"+ e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    @Override
public boolean edit(Persona per) {
    try {
        con = cn.getConnection();

        Map<String, Object> campos = new LinkedHashMap<>();
        campos.put("dpi", per.getDpi());
        campos.put("nombres", per.getNombres());
        campos.put("usuario", per.getUsuario());
        campos.put("rol", per.getRol());
        campos.put("contrasena", per.getContrasena());
        campos.put("correo", per.getCorreo());
        campos.put("lote", per.getLote());
        campos.put("numero_casa", per.getNumero_casa());
        campos.put("estado", per.getEstado());

        StringBuilder sql = new StringBuilder("UPDATE usuarios SET ");
        List<Object> params = new ArrayList<>();

        for (Map.Entry<String, Object> entry : campos.entrySet()) {
            Object valor = entry.getValue();
            if (valor != null && !valor.toString().trim().isEmpty()) {
                sql.append(entry.getKey()).append(" = ?, ");
                params.add(valor);
            }
        }

        if (params.isEmpty()) {
            // No hay campos para actualizar
            return false;
        }

        // Quitar última coma y espacio
        sql.setLength(sql.length() - 2);

        // Agregar condición WHERE
        sql.append(" WHERE id_usuario = ?");
        params.add(per.getId_usuario());

        ps = con.prepareStatement(sql.toString());
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
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
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";
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
    public Persona findByLogin(String usuario, String contrasena) {
        String sql = "SELECT id_usuario, dpi, nombres, usuario, correo, contrasena, rol, lote, numero_casa, estado FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            rs = ps.executeQuery();

            if (rs.next()) {
                Persona per = new Persona();
                per.setId_usuario(rs.getInt("id_usuario"));
                per.setDpi(rs.getString("dpi"));
                per.setNombres(rs.getString("nombres"));
                per.setUsuario(rs.getString("usuario"));
                per.setRol(rs.getString("rol"));
                per.setCorreo(rs.getString("correo"));
                per.setLote(rs.getString("lote"));
                per.setNumero_casa(rs.getString("numero_casa"));
                per.setEstado(rs.getString("estado"));
                return per;
            }
        } catch (Exception e) {
            System.err.println("Error en findByLogin: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public boolean existeUsuarioOCorreo(String usuario, String correo) {
    String sql = "SELECT COUNT(*) FROM Usuarios WHERE usuario = ? OR correo = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, correo);
        rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0; // true si existe
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public boolean existeUsuarioOCorreoEdit(String usuario, String correo, int id) {
    String sql = "SELECT COUNT(*) FROM Usuarios WHERE (usuario = ? OR correo = ?) AND id_usuario <> ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, usuario);
        ps.setString(2, correo);
        ps.setInt(3, id);
        rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}


}
