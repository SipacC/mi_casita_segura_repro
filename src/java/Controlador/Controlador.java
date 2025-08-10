package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class Controlador extends HttpServlet {

    // Rutas de vistas
    private static final String V_LISTAR = "vistas/listar.jsp";
    private static final String V_ADD    = "vistas/add.jsp";
    private static final String V_EDIT   = "vistas/edit.jsp";
    private static final String V_LOGIN  = "vistas/login.jsp";

    private final PersonaDAO dao = new PersonaDAO();

    // ---- GET: navegación y vistas ----
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        String destino;

        // Permitir ver el login sin sesión
        if ("Login".equalsIgnoreCase(accion)) {
            forward(request, response, V_LOGIN);
            return;
        }

        // Proteger TODO lo demás: si no hay sesión, enviar a login
        HttpSession ses = request.getSession(false);
        Persona usuario = (ses != null) ? (Persona) ses.getAttribute("usuario") : null;
        if (usuario == null) {
            forward(request, response, V_LOGIN);
            return;
        }

        if (accion == null || accion.isEmpty()) {
            // por defecto listar
            request.setAttribute("lista", dao.listar());
            forward(request, response, V_LISTAR);
            return;
        }

        switch (accion) {
            case "listar":
                request.setAttribute("lista", dao.listar());
                destino = V_LISTAR;
                break;

            case "add":
                destino = V_ADD;
                break;

            case "editar":
                // id requerido
                request.setAttribute("idper", request.getParameter("id"));
                destino = V_EDIT;
                break;

            case "eliminar": {
                String idStr = request.getParameter("id");
                if (idStr != null) {
                    try {
                        int id = Integer.parseInt(idStr);
                        dao.eliminar(id);
                    } catch (NumberFormatException ignored) {}
                }
                // luego de eliminar, volver a listar
                request.setAttribute("lista", dao.listar());
                destino = V_LISTAR;
                break;
            }

            case "Salir":
                if (ses != null) ses.invalidate();
                destino = V_LOGIN;
                break;

            default:
                // acción no reconocida → listar
                request.setAttribute("lista", dao.listar());
                destino = V_LISTAR;
        }

        forward(request, response, destino);
    }

    // ---- POST: acciones de formulario (login, agregar, actualizar) ----
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        if (accion == null) {
            doGet(request, response);
            return;
        }

        switch (accion) {
            case "Ingresar": {
                String dpi = request.getParameter("txtDpi");
                String contra = request.getParameter("txtContra");

                Persona usuario = dao.login(dpi, contra);
                if (usuario != null) {
                    HttpSession ses = request.getSession(true);
                    // No guardamos contraseña en sesión
                    usuario.setContra(null);
                    ses.setAttribute("usuario", usuario);
                    // Ir al listado
                    response.sendRedirect(request.getContextPath() + "/Controlador?accion=listar");
                } else {
                    request.setAttribute("error", "Credenciales inválidas");
                    forward(request, response, V_LOGIN);
                }
                return;
            }

            case "Agregar": {
                Persona p = new Persona();
                p.setDpi(request.getParameter("txtDpi"));
                p.setNom(request.getParameter("txtNom"));
                p.setRol(request.getParameter("txtRol"));
                p.setContra(request.getParameter("txtContra")); // (en producción: usa hash)

                dao.add(p);
                response.sendRedirect(request.getContextPath() + "/Controlador?accion=listar");
                return;
            }

            case "Actualizar": {
                Persona p = new Persona();
                try {
                    p.setId(Integer.parseInt(request.getParameter("txtid")));
                } catch (NumberFormatException ignored) {}
                p.setDpi(request.getParameter("txtDpi"));
                p.setNom(request.getParameter("txtNom"));
                p.setRol(request.getParameter("txtRol"));

                String nuevaContra = request.getParameter("txtContra");
                if (nuevaContra != null && !nuevaContra.trim().isEmpty()) {
                    p.setContra(nuevaContra);
                } else {
                    p.setContra(null); // para que DAO no la cambie
                }

                dao.edit(p);
                response.sendRedirect(request.getContextPath() + "/Controlador?accion=listar");
                return;
            }

            default:
                // resto de POST → reutiliza GET
                doGet(request, response);
        }
    }

    private void forward(HttpServletRequest req, HttpServletResponse resp, String vista)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(vista);
        rd.forward(req, resp);
    }

    @Override
    public String getServletInfo() {
        return "Controlador con login, sesión y CRUD de Persona";
    }
}
