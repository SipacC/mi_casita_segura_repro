package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import ModeloDAO.BitacoraDAO;
import Gestion_bitacora.RegistroBitacora;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Gestion_bitacora.RegistroBitacora;

public class ControladorLogin extends HttpServlet {

    PersonaDAO dao = new PersonaDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("accion");
        if (action == null || action.isEmpty()) {
            action = "login";
        }

        if (action.equalsIgnoreCase("login")) {
            
            RequestDispatcher vista = request.getRequestDispatcher("/vistasLogin/login.jsp");
            vista.forward(request, response);

        } else if (action.equalsIgnoreCase("logout")) {
            javax.servlet.http.HttpSession s = request.getSession(false);
            if (s != null) {
                Persona u = (Persona) s.getAttribute("usuario");
                if (u != null) {
                    RegistroBitacora.log(request, "Cierre de sesión", "Login");
                }
                s.invalidate();
            }
            response.sendRedirect("ControladorLogin?accion=login");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("accion");
        if ("validar".equalsIgnoreCase(action)) {
            String usuarioParam = request.getParameter("usuario");
            String contrasena = request.getParameter("contrasena");

            Persona usuario = dao.findByLogin(usuarioParam, contrasena);
            if (usuario != null) {
                request.getSession().invalidate();
                javax.servlet.http.HttpSession newSession = request.getSession(true);
                newSession.setAttribute("usuario", usuario);

                RegistroBitacora.log(request, "Inicio de sesión", "Login");

                String rol = usuario.getRol().toLowerCase();

                switch (rol) {
                    case "administrador":
                        response.sendRedirect("vistasAdmin/menuAdministrador.jsp");
                        break;
                    case "residente":
                        response.sendRedirect("vistasResidente/menuResidente.jsp");
                        break;
                    case "seguridad":
                        response.sendRedirect("vistasSeguridad/menuSeguridad.jsp");
                        break;
                    default:

                        newSession.invalidate();
                        request.setAttribute("error", "Rol no autorizado");
                        RequestDispatcher vista = request.getRequestDispatcher("/vistasLogin/login.jsp");
                        vista.forward(request, response);
                        break;
                }

            } else {
                request.setAttribute("error", "Nombre o contraseña incorrectos");
                RequestDispatcher vista = request.getRequestDispatcher("/vistasLogin/login.jsp");
                vista.forward(request, response);
            }
        } else {
            doGet(request, response);
        }
    }
}
