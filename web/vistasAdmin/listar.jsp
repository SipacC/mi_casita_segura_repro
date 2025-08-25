<%
if (session.getAttribute("usuario") == null) {
    // No hay sesión activa, redirige al login
    response.sendRedirect(request.getContextPath() + "/vistasLogin/login.jsp");
    return; // detener ejecución de la página
}
%>
<%@page import="java.util.Iterator"%>
<%@page import="Modelo.Persona"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
  Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
  if (usr == null) {
      response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
      return;
  }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container">
            <h1>Personas</h1>
            <a class="btn btn-success" href="ControladorAdmin?accion=add">Agregar Nuevo</a>

            <a href="${pageContext.request.contextPath}/vistasAdmin/menuAdministrador.jsp" class="btn btn-primary">
        Regresar al Menú
    </a>
            <br><br>
            <table class="table table-bordered">
                <thead>
                    <tr>
                        <th class="text-center">ID</th>
                        <th class="text-center">DPI</th>
                        <th class="text-center">NOMBRES</th>
                        <th class = "text-center">ROL</th>
                        <th class = "text-center">CONTRASEÑA</th>
                        <th class="text-center">ACCIONES</th>
                    </tr>
                </thead>
                <tbody>
                <%
                    List<Persona> list = (List<Persona>) request.getAttribute("lista");
                    if (list == null || list.isEmpty()) {
                %>
                        <tr>
                            <td colspan="6" class="text-center text-danger">No hay registros en la base de datos.</td>
                        </tr>
                <%
                    } else {
                        for (Persona per : list) {
                %>
                        <tr>
                            <td class="text-center"><%= per.getId() %></td>
                            <td class="text-center"><%= per.getDpi() %></td>
                            <td><%= per.getNom() %></td>
                            <td class = "text-center"><%= per.getRol() %></td>
                            <td class = "text-center"><%= per.getContrasena() %></td>
                            <td class="text-center">
                                <a class="btn btn-warning" href="ControladorAdmin?accion=editar&id=<%= per.getId() %>">Editar</a>
                                <a class="btn btn-danger" href="ControladorAdmin?accion=eliminar&id=<%= per.getId() %>">Remove</a>
                            </td>
                        </tr>
                <%
                        }
                    }
                %>
                </tbody>
            </table>
        </div>
    </body>
</html>
