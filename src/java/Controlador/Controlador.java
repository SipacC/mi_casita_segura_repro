package Controlador;

import Modelo.Persona;//de modelo importa a el dominio poo, de Persona
import ModeloDAO.PersonaDAO;//Dao para acceder a la base de datos
import java.io.IOException;//para excepciones de E/s
import java.io.PrintWriter;//para escribir respuestas de text por ejemplo en processRequest
import javax.servlet.RequestDispatcher;//para hacer forward a jsp
import javax.servlet.ServletException;//excepcion de servlets
import javax.servlet.http.HttpServlet;//clase base para servlets http
import javax.servlet.http.HttpServletRequest;//objeto solicitud
import javax.servlet.http.HttpServletResponse;//objeto de respuesta

// La clase Controlador ES un servlet porque extiende HttpServlet
public class Controlador extends HttpServlet {

    // Rutas (paths) a las JSP que vas a usar para mostrar vistas
    String listar="vistas/listar.jsp";
    String add="vistas/add.jsp";
    String edit="vistas/edit.jsp";

    // Instancias reutilizadas en todo el servlet
    // ⚠ OJO: en un servlet, estas variables de instancia se comparten entre hilos (no es thread-safe).
    Persona p =new Persona(); // Un objeto Persona reutilizado para operaciones (ver nota al final)
    PersonaDAO dao =new PersonaDAO();// DAO para CRUD contra la BD (reutilizado)
    int id;// Variable auxiliar para IDs (también compartida entre hilos)
    
    // Maneja peticiones HTTP GET (por URL o formularios con method="get")
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acceso="";// Aquí guardarás la JSP a la que harás forward
        String action=request.getParameter("accion");// Lee el parámetro 'accion' de la request
        if (action == null || action.isEmpty()) {
            action = "login";// Si no viene acción, por defecto muestra login
        }
        // ---- Ruteo por la acción ----
        if(action.equalsIgnoreCase("listar")){
            // Llama al DAO para traer la lista y la coloca como atributo para la JSP
            request.setAttribute("lista", dao.listar());
            acceso=listar;// JSP: vistas/listar.jsp
        }else if(action.equalsIgnoreCase("add")){
            // Solo navegar al formulario de alta
            acceso=add;
        }
        else if(action.equalsIgnoreCase("Agregar")){
            // Procesa el alta (aunque por semántica debería ser POST)
            String dpi = request.getParameter("txtDpi");
            String nom = request.getParameter("txtNom");
            String rol = request.getParameter("txtRol");
            String contrasena = request.getParameter("txtContrasena");
            // Rellena el objeto 'p' con los datos del formulario
            p.setDpi(dpi);
            p.setDpi(dpi);
            p.setNom(nom);
            p.setRol(rol);
            p.setContrasena(contrasena);
            dao.add(p);// Inserta en BD
            // Redirige a la lista (redirect = nueva request; evita reenvío del formulario)
            response.sendRedirect("Controlador?accion=listar");
            return;// Importante: terminar aquí para no seguir al forward

        }
        else if(action.equalsIgnoreCase("editar")){
            // Enviar a la JSP de edición, pasando el ID seleccionado como atributo
            request.setAttribute("idper",request.getParameter("id"));
            acceso=edit;
        }
        else if(action.equalsIgnoreCase("Actualizar")){
            // Procesa la actualización (idealmente debería ser POST)
            id=Integer.parseInt(request.getParameter("txtid"));
            String dpi=request.getParameter("txtDpi");
            String nom=request.getParameter("txtNom");
            String rol = request.getParameter("txtRol");
            String contrasena = request.getParameter("txtContrasena");
            // Asigna los nuevos valores al objeto 'p'
            p.setId(id);
            p.setDpi(dpi);
            p.setNom(nom);
            p.setRol(rol);

            // Si el campo contraseña viene vacío, decides no cambiarla
            if (contrasena != null && !contrasena.trim().isEmpty()) {
                p.setContrasena(contrasena);
            } else {
                p.setContrasena(null);
            }
            
            dao.edit(p);//actualiza en la base de datos
            //redirige a la lista
            response.sendRedirect("Controlador?accion=listar");
            return;
        }
        else if(action.equalsIgnoreCase("eliminar")){
            // Elimina por ID (también sería más correcto por POST)
            id=Integer.parseInt(request.getParameter("id"));
            p.setId(id);
            dao.eliminar(id);
            // Redirige a la lista
            response.sendRedirect("Controlador?accion=listar");
            return;
        }
        else if (action.equalsIgnoreCase("login")) {
            // Solo navega al login
            acceso = "vistas/login.jsp";
        }
        else if (action.equalsIgnoreCase("logout")) {
            // Cierra la sesión del usuario
            request.getSession().invalidate();
            // Y lo manda al login
            response.sendRedirect("Controlador?accion=login");
            return;
        }
        // ---- Protección de rutas: si no hay usuario en sesión y no es login/validar, manda a login ----
        if (request.getSession().getAttribute("usuario") == null 
                  && !(action.equalsIgnoreCase("login") || action.equalsIgnoreCase("validar"))) {
            response.sendRedirect("Controlador?accion=login");
            return;
        }

        // ---- Navegación a la vista (forward) ----
        // Forward NO cambia la URL y reutiliza la misma request/response.
        RequestDispatcher vista=request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    // Maneja peticiones HTTP POST (formularios con method="post")
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
                String action = request.getParameter("accion");// Lee 'accion' del formulario
                if ("validar".equalsIgnoreCase(action)){
                    // Lee credenciales enviadas desde el login.jsp
                    String nom = request.getParameter("nom");
                    String contrasena = request.getParameter("contrasena");
                    // Busca el usuario en BD
                    Persona usuario = dao.findByLogin(nom, contrasena);
                    if (usuario != null) {
                        // Por seguridad: invalida la sesión actual (si existía)
                        request.getSession().invalidate();
                        // Crea una sesión nueva y guarda el usuario logueado
                        javax.servlet.http.HttpSession newSession = request.getSession(true);
                        newSession.setAttribute("usuario", usuario);

                        //redireccion por rol
                        if ("administrador".equalsIgnoreCase(usuario.getRol())) {
                            // Va a un menú de admin (JSP directa)
                            response.sendRedirect("vistas/menuAdministrador.jsp");
                        } else {
                            // Usuarios normales van a la lista
                            response.sendRedirect("Controlador?accion=listar");
                        }
                    } else {
                        // Credenciales incorrectas: vuelve al login con un mensaje de error
                        request.setAttribute("error", "Nombre o contraseña incorrectos");
                        request.getRequestDispatcher("vistas/login.jsp").forward(request, response);      
                    }
                    return;
                }
                // Si no era 'validar', delega en doGet para manejar otras acciones por POST
                doGet(request, response);
            }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
