package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import ModeloDAO.BitacoraDAO;
import Gestion_bitacora.RegistroBitacora;


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
    String menuCamaras = "vistasAdmin/menuCamaras.jsp";
    String bitacora = "vistasAdmin/bitacora.jsp";
    Persona p = new Persona();
    PersonaDAO dao = new PersonaDAO();
    int id;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession(false) == null || request.getSession().getAttribute("usuario") == null) {
            response.sendRedirect(request.getContextPath() + "/vistasLogin/login.jsp");
            return;
        }

        String acceso = "";
        String action = request.getParameter("accion");

        if (action == null || action.isEmpty()) {
            action = "vistasAdmin/menuAdministrador.jsp";
        }

        if (action.equalsIgnoreCase("listar")) {
            request.setAttribute("lista", dao.listar());
            acceso = listar;

        } else if (action.equalsIgnoreCase("add")) {
            acceso = add;

        } else if (action.equalsIgnoreCase("Agregar")) {
            String usuario = request.getParameter("txtUsuario");
            String correo = request.getParameter("txtCorreo");

            if (dao.existeUsuarioOCorreo(usuario, correo)) {
                request.setAttribute("error", "El usuario o correo ya existe, por favor use otro.");
                acceso = add;
            } else {
                p.setDpi(request.getParameter("txtDpi"));
                p.setNombres(request.getParameter("txtNombres"));
                p.setUsuario(usuario);
                p.setCorreo(correo);
                p.setRol(request.getParameter("txtRol"));
                p.setLote(request.getParameter("txtLote"));
                p.setNumero_casa(request.getParameter("txtNumeroCasa"));
                p.setEstado(request.getParameter("txtEstado"));
                p.setContrasena(request.getParameter("txtContrasena"));

                int idNuevo = dao.add(p);

                if (idNuevo > 0) {
                    System.out.println("Usuario creado con ID " + idNuevo + " y QR generado.");
                    RegistroBitacora.log(request, "Creó un nuevo usuario con ID " + idNuevo, "Agregar usuario");
                } else {
                    System.err.println("Error al crear el usuario y generar QR.");
                }
                response.sendRedirect("ControladorAdmin?accion=listar");
                return;
            }

        } else if (action.equalsIgnoreCase("editar")) {
            request.setAttribute("idper", request.getParameter("id"));
            acceso = edit;

        } else if (action.equalsIgnoreCase("Actualizar")) {
            id = Integer.parseInt(request.getParameter("txtid"));
            p.setId_usuario(id);

            String usuario = request.getParameter("txtUsuario");
            String correo = request.getParameter("txtCorreo");

            if (dao.existeUsuarioOCorreoEdit(usuario, correo, id)) {
                request.setAttribute("error", "El usuario o correo ya existe, por favor use otro.");
                request.setAttribute("idper", id);
            } else {
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
                RegistroBitacora.log(request, "Actualizó datos del usuario con ID " + p.getId_usuario(), "Editar Usuario");
                response.sendRedirect("ControladorAdmin?accion=listar");
                return;
            }

        } else if (action.equalsIgnoreCase("eliminar")) {
            id = Integer.parseInt(request.getParameter("id"));
            dao.eliminar(id);
            RegistroBitacora.log(request, "Eliminó al usuario con ID " + id, "Usuarios");
            response.sendRedirect("ControladorAdmin?accion=listar");
            return;

        } else if (action.equalsIgnoreCase("menuCamaras")) {
            acceso = menuCamaras;
        } else if (action.equalsIgnoreCase("verBitacora")) {
            String filtroUsuario = request.getParameter("usuario");
            String filtroModulo = request.getParameter("modulo");

            Integer idUsuario = null;
            if (filtroUsuario != null && !filtroUsuario.trim().isEmpty()) {
                try {
                    idUsuario = Integer.parseInt(filtroUsuario);
                } catch (Exception e) {
                    idUsuario = null;
                }
            }

            BitacoraDAO bitacoraDAO = new BitacoraDAO();
            request.setAttribute("listaBitacora", bitacoraDAO.listar(idUsuario, filtroModulo));

            acceso = bitacora;
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
