package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ControladorAdmin extends HttpServlet {

    String listar = "vistasAdmin/listar.jsp";
    String add = "vistasAdmin/add.jsp";
    String edit = "vistasAdmin/edit.jsp";
    Persona p = new Persona();
    PersonaDAO dao = new PersonaDAO();
    int id;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acceso = "";
        String action = request.getParameter("accion");

        if (action == null || action.isEmpty()) {
            action = "listar";
        }

        // CRUD y menú (manteniendo lógica original)
        if (action.equalsIgnoreCase("listar")) {
            request.setAttribute("lista", dao.listar());
            acceso = listar;
        } else if (action.equalsIgnoreCase("add")) {
            acceso = add;
        } else if (action.equalsIgnoreCase("Agregar")) {
            p.setDpi(request.getParameter("txtDpi"));
            p.setNom(request.getParameter("txtNom"));
            p.setRol(request.getParameter("txtRol"));
            p.setContrasena(request.getParameter("txtContrasena"));
            dao.add(p);
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;
        } else if (action.equalsIgnoreCase("editar")) {
            request.setAttribute("idper", request.getParameter("id"));
            acceso = edit;
        } else if (action.equalsIgnoreCase("Actualizar")) {
            id = Integer.parseInt(request.getParameter("txtid"));
            p.setId(id);
            p.setDpi(request.getParameter("txtDpi"));
            p.setNom(request.getParameter("txtNom"));
            p.setRol(request.getParameter("txtRol"));
            String contrasena = request.getParameter("txtContrasena");
            p.setContrasena((contrasena != null && !contrasena.trim().isEmpty()) ? contrasena : null);
            dao.edit(p);
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;
        } else if (action.equalsIgnoreCase("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.eliminar(id);
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;
        }

        // Verificación de sesión
        if (request.getSession().getAttribute("usuario") == null) {
            response.sendRedirect("ControladorLogin?accion=login");
            return;
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
