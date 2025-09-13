package Controlador;

import Modelo.Persona;
import Modelo.Pago;
import Modelo.TipoPago;
import Modelo.Tarjeta;
import Modelo.Reserva;

import ModeloDAO.PagoDAO;
import ModeloDAO.TipoPagoDAO;
import ModeloDAO.TarjetaDAO;
import ModeloDAO.ReservaDAO;

import Gestion_factura.CrearFactura;
import Gestion_factura.CorreoEnviarFactura;
import Gestion_reserva.CrearReservaPDF;
import Gestion_reserva.CorreoEnviarReserva;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

public class ControladorResidente extends HttpServlet {

    // Rutas JSP
    String menuResidente = "vistasResidente/menuResidente.jsp";
    String pagos = "vistasResidente/pagos.jsp";
    String registrarPago = "vistasResidente/registrarPago.jsp";
    String consultarPago = "vistasResidente/consultarPago.jsp";
    String reservas = "vistasResidente/reservas.jsp";
    String crearReserva = "vistasResidente/crearReserva.jsp";

    // DAOs
    PagoDAO pagoDAO = new PagoDAO();
    TipoPagoDAO tipoDAO = new TipoPagoDAO();
    TarjetaDAO tarjetaDAO = new TarjetaDAO();
    ReservaDAO reservaDAO = new ReservaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Persona usr = (Persona) (session != null ? session.getAttribute("usuario") : null);

        if (usr == null || !"residente".equalsIgnoreCase(usr.getRol())) {
            response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
            return;
        }

        String action = request.getParameter("accion");
        String acceso = menuResidente;

        if ("pagos".equalsIgnoreCase(action)) {
            request.setAttribute("listaPagos", pagoDAO.listarPorUsuario(usr.getId_usuario()));
            acceso = pagos;

        } else if ("registrarPago".equalsIgnoreCase(action)) {
            acceso = registrarPago;

        } else if ("guardarPago".equalsIgnoreCase(action)) {
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
                pago.setEstado("Pendiente");
                int idPago = pagoDAO.registrarPago(pago);

                if (idPago > 0) {
                    String numeroFactura = "FAC-" + idPago;
                    String rutaFactura = CrearFactura.generarFactura(pago, usr, numeroFactura);

                    if (rutaFactura != null) {
                        CorreoEnviarFactura correo = new CorreoEnviarFactura();
                        correo.enviarFactura(
                                usr.getCorreo(),
                                usr.getNombres(),
                                rutaFactura,
                                "Pago en efectivo (" + nombreTipo + ")"
                        );
                    }

                    request.setAttribute("mensaje", "Factura generada y enviada, pendiente de pago en efectivo.");
                } else {
                    request.setAttribute("error", "Error al registrar el pago en efectivo.");
                }

            } else if ("Tarjeta".equalsIgnoreCase(metodo)) {
                String numero = request.getParameter("numero");
                String nombre = request.getParameter("nombre");
                String cvv = request.getParameter("cvv");
                double total = pago.getMonto() + pago.getMora();

                Tarjeta tarjeta = tarjetaDAO.buscarTarjeta(numero, nombre, cvv);

                if (tarjeta != null) {
                    boolean valida = tarjetaDAO.validarTarjeta(numero, nombre, cvv, total);
                    if (valida) {
                        tarjetaDAO.descontarSaldo(numero, total);
                        pago.setEstado("Confirmado");
                        int idPago = pagoDAO.registrarPago(pago);

                        if (idPago > 0) {
                            String numeroFactura = "FAC-" + idPago;
                            String rutaFactura = CrearFactura.generarFactura(pago, usr, numeroFactura);

                            if (rutaFactura != null) {
                                CorreoEnviarFactura correo = new CorreoEnviarFactura();
                                correo.enviarFactura(
                                        usr.getCorreo(),
                                        usr.getNombres(),
                                        rutaFactura,
                                        "Pago con tarjeta (" + nombreTipo + ")"
                                );
                            }

                            request.setAttribute("mensaje", "Pago realizado con tarjeta. Factura enviada por correo.");
                        } else {
                            request.setAttribute("error", "Error al registrar el pago con tarjeta.");
                        }
                    } else {
                        request.setAttribute("error", "Tarjeta vencida o saldo insuficiente.");
                        acceso = consultarPago;
                    }
                } else {
                    request.setAttribute("error", "Tarjeta no encontrada.");
                    acceso = consultarPago;
                }
            }

            request.setAttribute("listaPagos", pagoDAO.listarPorUsuario(usr.getId_usuario()));
            acceso = pagos;

        } else if ("consultarPago".equalsIgnoreCase(action)) {
            String tipoPago = request.getParameter("tipo_pago");
            double total = pagoDAO.calcularMontoConMora(
                    usr.getId_usuario(),
                    tipoPago,
                    usr.getFecha_creacion()
            );

            request.setAttribute("tipoPago", tipoPago);
            request.setAttribute("totalPago", total);
            acceso = consultarPago;
        }

        else if ("reservas".equalsIgnoreCase(action)) {
            String buscar = request.getParameter("buscar");
            List<Reserva> lista;
            if (buscar != null && !buscar.trim().isEmpty()) {
                lista = reservaDAO.buscarPorNombre(usr.getId_usuario(), buscar);
                request.setAttribute("busqueda", buscar);
            } else {
                lista = reservaDAO.listarPorUsuario(usr.getId_usuario());
            }

            request.setAttribute("listaReservas", lista);
            acceso = reservas;

        } else if ("crearReserva".equalsIgnoreCase(action)) {
            acceso = crearReserva;

        } else if ("guardarReserva".equalsIgnoreCase(action)) {
            Reserva r = new Reserva();
            r.setId_usuario(usr.getId_usuario());
            r.setId_area(Integer.parseInt(request.getParameter("id_area")));
            r.setFecha_reserva(request.getParameter("fecha"));
            r.setHora_inicio(request.getParameter("hora_inicio"));
            r.setHora_fin(request.getParameter("hora_fin"));
            r.setEstado("Activa");

            if (reservaDAO.validarDisponibilidad(r)) {
                int idReserva = reservaDAO.insertar(r);

                if (idReserva > 0) {
                    r.setId_reserva(idReserva);

                    String rutaPDF = CrearReservaPDF.generarReserva(
                            r,
                            request.getParameter("nombre_area"),
                            usr.getNombres()
                    );

                    if (rutaPDF != null) {
                        CorreoEnviarReserva.enviarCorreo(
                                usr.getCorreo(),
                                "Confirmación de reserva #" + r.getId_reserva(),
                                "Estimado " + usr.getNombres() + ", su reserva ha sido confirmada exitosamente. 🎉",
                                rutaPDF
                        );
                        request.setAttribute("mensaje", "Reserva creada y comprobante enviado al correo.");
                    } else {
                        request.setAttribute("error", "No se pudo generar el PDF de la reserva.");
                    }
                } else {
                    request.setAttribute("error", "Error al guardar la reserva.");
                }
            } else {
                request.setAttribute("error", "El área no está disponible en ese horario.");
            }

            request.setAttribute("listaReservas", reservaDAO.listarPorUsuario(usr.getId_usuario()));

            acceso = reservas;

        } else if ("cancelarReserva".equalsIgnoreCase(action)) {
            int idReserva = Integer.parseInt(request.getParameter("id"));
            reservaDAO.cancelar(idReserva);
            request.setAttribute("mensaje", "Reserva cancelada con éxito.");

            request.setAttribute("listaReservas", reservaDAO.listarPorUsuario(usr.getId_usuario()));
            
            acceso = reservas;

        } else {
            acceso = menuResidente;
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
