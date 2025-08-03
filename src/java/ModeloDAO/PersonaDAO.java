
package ModeloDAO;

import Config.Conexion;
import Intefaces.CRUD;
import Modelo.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class PersonaDAO implements CRUD{
    Conexion cn=new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    Persona p=new Persona();
    
    @Override
public List<Persona> listar() {
    List<Persona> list = new ArrayList<>();
    String sql = "SELECT * FROM persona";

    try {
        con = cn.getConnection();
        System.out.println("⏺ Conexión: " + con); // Ver si la conexión es válida
        ps = con.prepareStatement(sql);
        rs = ps.executeQuery();
        while (rs.next()) {
            Persona per = new Persona();
            per.setId(rs.getInt("id"));
            per.setDpi(rs.getString("dpi"));
            per.setNom(rs.getString("nombres"));
            list.add(per);
        }
        System.out.println("✅ Registros encontrados: " + list.size());
    } catch (Exception e) {
        System.err.println("❌ Error en listar(): " + e.getMessage());
        e.printStackTrace();
    }

    return list;
}


    @Override
    public Persona list(int id) {
        String sql="select * from persona where id="+id;
        try {
            con=cn.getConnection();
            ps=con.prepareStatement(sql);
            rs=ps.executeQuery();
            while(rs.next()){                
                p.setId(rs.getInt("id"));
                p.setDpi(rs.getString("dpi"));
                p.setNom(rs.getString("nombres"));
                
            }
        } catch (Exception e) {
        }
        return p;
    }

    @Override
    public boolean add(Persona per) {
       String sql="insert into persona(dpi, nombres)values(?, ?)";
        try {
            con=cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.setString(1, per.getDpi());
            ps.setString(2, per.getNom());
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
       //return false;
    }

    @Override
    public boolean edit(Persona per) {
        String sql="update persona set dpi='"+per.getDpi()+"',nombres='"+per.getNom()+"'where id="+per.getId();
        try {
            con=cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
        }
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        String sql="delete from persona where id="+id;
        try {
            con=cn.getConnection();
            ps=con.prepareStatement(sql);
            ps.executeUpdate();
        } catch (Exception e) {
        }
        return false;
    }
    
}
