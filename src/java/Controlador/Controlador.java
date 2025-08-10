package Controlador;

import Modelo.Persona;
import ModeloDAO.PersonaDAO;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Controlador extends HttpServlet {

    String listar="vistas/listar.jsp";
    String add="vistas/add.jsp";
    String edit="vistas/edit.jsp";
    Persona p=new Persona();
    PersonaDAO dao=new PersonaDAO();
    int id;
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Controlador</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Controlador at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String acceso="";
        String action=request.getParameter("accion");
        if (action == null ) {
            action = "listar";
        }
        if(action.equalsIgnoreCase("listar")){
            request.setAttribute("lista", dao.listar());
            acceso=listar;            
        }else if(action.equalsIgnoreCase("add")){
            acceso=add;
        }
        else if(action.equalsIgnoreCase("Agregar")){
            String dpi = request.getParameter("txtDpi");
            String nom = request.getParameter("txtNom");
            String rol = request.getParameter("txtRol");
            String contrasena = request.getParameter("txtContrasena");
            p.setDpi(dpi);
            p.setNom(nom);
            p.setRol(rol);
            p.setContrasena(contrasena);
            dao.add(p);
            response.sendRedirect("Controlador?accion=listar");
            return;
            //acceso=listar;
        }
        else if(action.equalsIgnoreCase("editar")){
            request.setAttribute("idper",request.getParameter("id"));
            acceso=edit;
        }
        else if(action.equalsIgnoreCase("Actualizar")){
            id=Integer.parseInt(request.getParameter("txtid"));
            String dpi=request.getParameter("txtDpi");
            String nom=request.getParameter("txtNom");
            String rol = request.getParameter("txtRol");
            String contrasena = request.getParameter("txtContrasena");
            p.setId(id);
            p.setDpi(dpi);
            p.setNom(nom);
            p.setRol(rol);

            if (contrasena != null && !contrasena.trim().isEmpty()) {
                p.setContrasena(contrasena);
            } else {
                p.setContrasena(null);
            }
            
            dao.edit(p);
            response.sendRedirect("Controlador?accion=listar");
            return;
            //acceso=listar;
        }
        else if(action.equalsIgnoreCase("eliminar")){
            id=Integer.parseInt(request.getParameter("id"));
            p.setId(id);
            dao.eliminar(id);
            //acceso=listar;
            response.sendRedirect("Controlador?accion=listar");
            return;
        }
        RequestDispatcher vista=request.getRequestDispatcher(acceso);
        vista.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
