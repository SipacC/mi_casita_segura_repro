package Controlador;

import Modelo.Persona;
import Modelo.Pago;
import Modelo.TipoPago;
import Modelo.Tarjeta;
import ModeloDAO.PagoDAO;
import ModeloDAO.TipoPagoDAO;
import ModeloDAO.TarjetaDAO;

import Gestion_factura.CrearFactura;
import Gestion_factura.CorreoEnviarFactura;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ControladorResidente extends HttpServlet {

    String menuResidente = "vistasResidente/menuResidente.jsp";
    String pagos = "vistasResidente/pagos.jsp";
    String registrarPago = "vistasResidente/registrarPago.jsp";
    String consultarPago = "vistasResidente/consultarPago.jsp";
    String reservas = "vistasResidente/reservas.jsp"; // lo harás después

    PagoDAO pagoDAO = new PagoDAO();
    TipoPagoDAO tipoDAO = new TipoPagoDAO();
    TarjetaDAO tarjetaDAO = new TarjetaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Persona usr = (Persona) (session != null ? session.getAttribute("usuario") : null);

        // 🔹 Validación de sesión y rol
        if (usr == null || !"residente".equalsIgnoreCase(usr.getRol())) {
            response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
            return;
        }

        String action = request.getParameter("accion");
        String acceso = menuResidente; // default: menú residente

        if ("pagos".equalsIgnoreCase(action)) {
            // Mostrar lista de pagos realizados
            request.setAttribute("listaPagos", pagoDAO.listarPorUsuario(usr.getId_usuario()));
            acceso = pagos;

        } else if ("registrarPago".equalsIgnoreCase(action)) {
            // Mostrar formulario para registrar un nuevo pago
            acceso = registrarPago;

        } else if ("guardarPago".equalsIgnoreCase(action)) {
            // Capturar datos del formulario
            String nombreTipo = request.getParameter("tipo_pago");
            TipoPago tp = tipoDAO.buscarPorNombre(nombreTipo);

            Pago pago = new Pago();
            pago.setId_usuario(usr.getId_usuario());
            pago.setId_tipo(tp.getId_tipo());
            pago.setMetodo_pago(request.getParameter("metodo_pago"));
            pago.setMonto(Double.parseDouble(request.getParameter("monto")));
            pago.setMora(Double.parseDouble(request.getParameter("mora")));
            pago.setObservaciones(request.getParameter("observaciones"));

            String metodo = request.getParameter("metodo_pago");

            if ("Efectivo".equalsIgnoreCase(metodo)) {
                // 🔹 En efectivo → estado pendiente
                pago.setEstado("Pendiente");

                int idPago = pagoDAO.registrarPago(pago);
                if (idPago > 0) {
                    String numeroFactura = "FAC-" + idPago;

                    // ✅ Generar factura
                    String rutaFactura = CrearFactura.generarFactura(pago, usr, numeroFactura);

                    // ✅ Enviar factura
                    CorreoEnviarFactura correo = new CorreoEnviarFactura();
                    correo.enviarFactura(
                            usr.getCorreo(),
                            usr.getNombres(),
                            rutaFactura,
                            "Pago en efectivo (" + nombreTipo + ")"
                    );

                    request.setAttribute("mensaje", "📄 Factura generada y enviada, pendiente de pago en efectivo.");
                } else {
                    request.setAttribute("error", "❌ Error al registrar el pago en efectivo.");
                }

            } else if ("Tarjeta".equalsIgnoreCase(metodo)) {
                // 🔹 En tarjeta → validar contra BD
                String numero = request.getParameter("numero");
                String nombre = request.getParameter("nombre");
                String expira = request.getParameter("expira"); // MM/AA
                String cvv = request.getParameter("cvv");
                double total = pago.getMonto() + pago.getMora();

                // Buscar tarjeta
                Tarjeta tarjeta = tarjetaDAO.buscarTarjeta(numero, nombre, cvv);

                if (tarjeta != null) {
                    // Validar vencimiento y saldo
                    boolean valida = tarjetaDAO.validarTarjeta(numero, nombre, cvv, total);
                    if (valida) {
                        // Descontar saldo
                        tarjetaDAO.descontarSaldo(numero, total);

                        pago.setEstado("Confirmado");
                        int idPago = pagoDAO.registrarPago(pago);

                        if (idPago > 0) {
                            String numeroFactura = "FAC-" + idPago;

                            // ✅ Generar factura
                            String rutaFactura = CrearFactura.generarFactura(pago, usr, numeroFactura);

                            // ✅ Enviar factura
                            CorreoEnviarFactura correo = new CorreoEnviarFactura();
                            correo.enviarFactura(
                                    usr.getCorreo(),
                                    usr.getNombres(),
                                    rutaFactura,
                                    "Pago con tarjeta (" + nombreTipo + ")"
                            );

                            request.setAttribute("mensaje", "✅ Pago realizado con tarjeta. Factura enviada por correo.");
                        } else {
                            request.setAttribute("error", "❌ Error al registrar el pago con tarjeta.");
                        }
                    } else {
                        request.setAttribute("error", "⚠️ Tarjeta vencida o saldo insuficiente.");
                        acceso = consultarPago;
                    }
                } else {
                    request.setAttribute("error", "⚠️ Tarjeta no encontrada.");
                    acceso = consultarPago;
                }
            }

            // Siempre mostrar lista de pagos después
            request.setAttribute("listaPagos", pagoDAO.listarPorUsuario(usr.getId_usuario()));
            acceso = pagos;

        } else if ("consultarPago".equalsIgnoreCase(action)) {
            // Consultar cuánto debe el usuario (usando fecha_creacion)
            String tipoPago = request.getParameter("tipo_pago");
            double total = pagoDAO.calcularMontoConMora(
                    usr.getId_usuario(),
                    tipoPago,
                    usr.getFecha_creacion()
            );

            request.setAttribute("tipoPago", tipoPago);
            request.setAttribute("totalPago", total);
            acceso = consultarPago;

        } else if ("reservas".equalsIgnoreCase(action)) {
            acceso = reservas;

        } else {
            acceso = menuResidente; // default
        }

        RequestDispatcher vista = request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
