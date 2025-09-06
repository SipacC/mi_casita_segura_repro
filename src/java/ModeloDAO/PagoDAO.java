package ModeloDAO;

import Config.Conexion;
import Modelo.Pago;
import Modelo.TipoPago;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class PagoDAO {
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // 🔹 Registrar un nuevo pago
    // 🔹 Registrar un nuevo pago y devolver el ID generado
public int registrarPago(Pago pago) {
    String sql = "INSERT INTO Pago (id_usuario, id_tipo, metodo_pago, fecha_pago, monto, mora, observaciones, estado) " +
                 "VALUES (?, ?, ?, NOW(), ?, ?, ?, ?)";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // 🔹 importante
        ps.setInt(1, pago.getId_usuario());
        ps.setInt(2, pago.getId_tipo());
        ps.setString(3, pago.getMetodo_pago());
        ps.setDouble(4, pago.getMonto());
        ps.setDouble(5, pago.getMora());
        ps.setString(6, pago.getObservaciones());
        ps.setString(7, pago.getEstado());

        int rows = ps.executeUpdate();

        if (rows > 0) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // 🔹 devolvemos id_pago generado
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1; // 🔹 si falla
}


    // 🔹 Listar todos los pagos de un usuario
    public List<Pago> listarPorUsuario(int idUsuario) {
        List<Pago> lista = new ArrayList<>();
        String sql = "SELECT * FROM Pago WHERE id_usuario = ? ORDER BY fecha_pago DESC";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setMora(rs.getDouble("mora"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    // 🔹 Calcular monto con mora (Mantenimiento) o devolver base (Multa/Reinstalación)
    public double calcularMontoConMora(int idUsuario, String tipoPago, java.sql.Date fechaCreacionUsuario) {
        TipoPagoDAO tipoDAO = new TipoPagoDAO();
        TipoPago tipo = tipoDAO.buscarPorNombre(tipoPago);

        if (tipo == null) {
            System.err.println("❌ Tipo de pago no encontrado: " + tipoPago);
            return 0;
        }

        double montoBase = tipo.getMonto();

        // ✅ Caso Mantenimiento
        if ("Mantenimiento".equalsIgnoreCase(tipoPago)) {
            String sql = "SELECT MAX(fecha_pago) AS ultimo_pago " +
                         "FROM Pago WHERE id_usuario = ? AND id_tipo = ?";
            try {
                con = cn.getConnection();
                ps = con.prepareStatement(sql);
                ps.setInt(1, idUsuario);
                ps.setInt(2, tipo.getId_tipo());
                rs = ps.executeQuery();

                LocalDate mesPendiente;

                if (rs.next() && rs.getDate("ultimo_pago") != null) {
                    mesPendiente = rs.getDate("ultimo_pago").toLocalDate().plusMonths(1);
                } else {
                    mesPendiente = fechaCreacionUsuario.toLocalDate();
                }

                // Calcular mora
                LocalDate hoy = LocalDate.now();
                LocalDate limite = mesPendiente.withDayOfMonth(5);
                long diasRetraso = ChronoUnit.DAYS.between(limite, hoy);

                double mora = diasRetraso > 0 ? diasRetraso * 25.0 : 0;

                return montoBase + mora;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ✅ Caso Multa / Reinstalación → solo monto base
        return montoBase;
    }

    // 🔹 Obtener el último pago de un usuario por tipo
    public Pago obtenerUltimoPago(int idUsuario, String tipoPago) {
        TipoPagoDAO tipoDAO = new TipoPagoDAO();
        TipoPago tipo = tipoDAO.buscarPorNombre(tipoPago);

        if (tipo == null) return null;

        String sql = "SELECT * FROM Pago WHERE id_usuario = ? AND id_tipo = ? ORDER BY fecha_pago DESC LIMIT 1";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            ps.setInt(2, tipo.getId_tipo());
            rs = ps.executeQuery();
            if (rs.next()) {
                Pago p = new Pago();
                p.setId_pago(rs.getInt("id_pago"));
                p.setId_usuario(rs.getInt("id_usuario"));
                p.setId_tipo(rs.getInt("id_tipo"));
                p.setMetodo_pago(rs.getString("metodo_pago"));
                p.setFecha_pago(rs.getTimestamp("fecha_pago"));
                p.setMonto(rs.getDouble("monto"));
                p.setMora(rs.getDouble("mora"));
                p.setObservaciones(rs.getString("observaciones"));
                p.setEstado(rs.getString("estado"));
                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
