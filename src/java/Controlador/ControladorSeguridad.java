package Controlador;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class ControladorSeguridad extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        HttpSession session = req.getSession(false);

        if (session == null || !"seguridad".equalsIgnoreCase((String) session.getAttribute("rol"))) {
            resp.sendRedirect("vistasLogin/accesoDenegado.jsp");
            return;
        }

        String accion = req.getParameter("accion");
        if (accion == null) accion = "menu";

        switch (accion) {
            case "menu":
                req.getRequestDispatcher("vistasSeguridad/menuSeguridad.jsp").forward(req, resp);
                break;
            case "controlEntradas":
                req.getRequestDispatcher("vistasSeguridad/controlEntradas.jsp").forward(req, resp);
                break;
            case "controlSalidas":
                req.getRequestDispatcher("vistasSeguridad/controlSalidas.jsp").forward(req, resp);
                break;
            default:
                req.getRequestDispatcher("vistasSeguridad/menuSeguridad.jsp").forward(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
}
