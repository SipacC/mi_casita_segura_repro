package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

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

        if (request.getSession(false) == null || request.getSession().getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/vistasLogin/login.jsp");
            return; // detener ejecución
        }

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
            p.setNombres(request.getParameter("txtNombres"));
            p.setUsuario(request.getParameter("txtUsuario"));
            p.setCorreo(request.getParameter("txtCorreo"));
            p.setRol(request.getParameter("txtRol"));
            p.setLote(request.getParameter("txtLote"));
            p.setNumero_casa(request.getParameter("txtNumeroCasa"));
            p.setEstado(request.getParameter("txtEstado"));
            p.setContrasena(request.getParameter("txtContrasena"));
            dao.add(p);
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;
        } else if (action.equalsIgnoreCase("editar")) {
            request.setAttribute("idper", request.getParameter("id"));
            acceso = edit;
        } else if (action.equalsIgnoreCase("Actualizar")) {
            id = Integer.parseInt(request.getParameter("txtid"));
            p.setId_usuario(id);

            Map<String, java.util.function.Consumer<String>> setters = new LinkedHashMap<>();
            setters.put("txtDpi", valor -> p.setDpi(valor));
            setters.put("txtNombres", valor -> p.setNombres(valor));
            setters.put("txtUsuario", valor -> p.setUsuario(valor));
            setters.put("txtCorreo", valor -> p.setCorreo(valor));
            setters.put("txtRol", valor -> p.setRol(valor));
            setters.put("txtLote", valor -> p.setLote(valor));
            setters.put("txtNumeroCasa", valor -> p.setNumero_casa(valor));
            setters.put("txtEstado", valor -> p.setEstado(valor));
            setters.put("txtContrasena", valor -> p.setContrasena(valor));

            for (Map.Entry<String, java.util.function.Consumer<String>> entry : setters.entrySet()) {
                String valor = request.getParameter(entry.getKey());
                if (valor != null && !valor.trim().isEmpty()) {
                    entry.getValue().accept(valor);
                }
            }

            dao.edit(p);
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;
        } else if (action.equalsIgnoreCase("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.eliminar(id);
            response.sendRedirect("ControladorAdmin?accion=listar");
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
