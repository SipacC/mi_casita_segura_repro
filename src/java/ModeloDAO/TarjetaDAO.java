package ModeloDAO;

import Config.Conexion;
import Modelo.Tarjeta;

import java.sql.*;

public class TarjetaDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public Tarjeta buscarTarjeta(String numero, String nombreTitular, String cvv) {
        String sql = "SELECT * FROM tarjeta WHERE numero_tarjeta=? AND nombre_titular=? AND cvv=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, numero);
            ps.setString(2, nombreTitular);
            ps.setString(3, cvv);

            rs = ps.executeQuery();
            if (rs.next()) {
                Tarjeta t = new Tarjeta();
                t.setId_tarjeta(rs.getInt("id_tarjeta"));
                t.setId_usuario(rs.getInt("id_usuario"));
                t.setNumero_tarjeta(rs.getString("numero_tarjeta"));
                t.setFecha_vencimiento(rs.getDate("fecha_vencimiento"));
                t.setCvv(rs.getString("cvv"));
                t.setNombre_titular(rs.getString("nombre_titular"));
                t.setTipo_tarjeta(rs.getString("tipo_tarjeta"));
                t.setSaldo(rs.getDouble("saldo"));
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Validar si la tarjeta es válida y si tiene saldo suficiente
    public boolean validarTarjeta(String numero, String nombreTitular, String cvv, double monto) {
        Tarjeta t = buscarTarjeta(numero, nombreTitular, cvv);
        if (t == null) {
            System.err.println("Tarjeta no encontrada");
            return false;
        }

        
        java.time.LocalDate hoy = java.time.LocalDate.now();
        if (t.getFecha_vencimiento().toLocalDate().isBefore(hoy)) {
            System.err.println("Tarjeta vencida");
            return false;
        }

        // Verificar saldo
        if (t.getSaldo() < monto) {
            System.err.println("Saldo insuficiente");
            return false;
        }

        return true;
    }

    // Descontar saldo
    public boolean descontarSaldo(String numero, double monto) {
        String sql = "UPDATE tarjeta SET saldo = saldo - ? WHERE numero_tarjeta=?";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setDouble(1, monto);
            ps.setString(2, numero);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //Registrar nueva tarjeta
    public boolean registrarTarjeta(Tarjeta t) {
        String sql = "INSERT INTO tarjeta (id_usuario, numero_tarjeta, fecha_vencimiento, cvv, nombre_titular, tipo_tarjeta, saldo) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, t.getId_usuario());
            ps.setString(2, t.getNumero_tarjeta());
            ps.setDate(3, t.getFecha_vencimiento());
            ps.setString(4, t.getCvv());
            ps.setString(5, t.getNombre_titular());
            ps.setString(6, t.getTipo_tarjeta());
            ps.setDouble(7, t.getSaldo());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
