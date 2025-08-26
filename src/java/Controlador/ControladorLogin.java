package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
            // Modificado: apunta a la nueva carpeta de vistas
            RequestDispatcher vista = request.getRequestDispatcher("/vistasLogin/login.jsp");
            vista.forward(request, response);
        } else if (action.equalsIgnoreCase("logout")) {
            request.getSession().invalidate();
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

                // Redirección según rol (sin modificar lógica)
                if ("administrador".equalsIgnoreCase(usuario.getRol())) {
                    response.sendRedirect("vistasAdmin/menuAdministrador.jsp");
                } else {
                    response.sendRedirect("ControladorAdmin?accion=listar");
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
