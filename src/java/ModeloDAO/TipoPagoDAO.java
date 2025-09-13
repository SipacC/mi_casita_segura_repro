package ModeloDAO;

import Config.Conexion;
import Modelo.TipoPago;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoPagoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public List<TipoPago> listar() {
        List<TipoPago> lista = new ArrayList<>();
        String sql = "SELECT * FROM TipoPago ORDER BY id_tipo";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TipoPago tp = new TipoPago();
                tp.setId_tipo(rs.getInt("id_tipo"));
                tp.setNombre(rs.getString("nombre"));
                tp.setMonto(rs.getDouble("monto"));
                lista.add(tp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public TipoPago buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM TipoPago WHERE nombre = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new TipoPago(
                    rs.getInt("id_tipo"),
                    rs.getString("nombre"),
                    rs.getDouble("monto")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public TipoPago buscarPorId(int id_tipo) {
        String sql = "SELECT * FROM TipoPago WHERE id_tipo = ?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id_tipo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new TipoPago(
                    rs.getInt("id_tipo"),
                    rs.getString("nombre"),
                    rs.getDouble("monto")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
