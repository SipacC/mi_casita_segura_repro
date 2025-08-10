
<%@page import="Modelo.Persona"%>
<%@page import="ModeloDAO.PersonaDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
        <title>JSP Page</title>
    </head>
    <body>
        <div class="container">
            <div class="col-lg-6">
              <%
              PersonaDAO dao=new PersonaDAO();
              String idStr = (String) request.getAttribute("idper");
              if (idStr == null) { idStr = request.getParameter("id"); }
              int id=Integer.parseInt(idStr);
              Persona p=(Persona)dao.list(id);
          %>
            <h1>Modificar Persona</h1>
            <form action="Controlador" method="get" autocomplete="off">
                <input type="hidden" name="accion" value="Actulaizar">
                <input type="hidden" name="txtid" value="<%= p.getId()%>">
                <label for="dpi">DPI:</label>
                <input id="dpi" class="form-control" type="text" name="txtDpi"
                       value="<%=p.getDpi()%>" required><br>
                <label for="nom">Nombres:</label>
                <input id="nom" class="form-control" type="text" name="txtNom"
                         value="<%= p.getNom()%>" required><br>
                <label for="rol">Rol:</label>
                <select id="rol" class="form-control" name="txtRol" required>
                    <option value="">Seleccione</option>
                    <option value="usuario" <%= "usuario".equalsIgnoreCase(p.getRol() == null ? "" : p.getRol()) ? "selected" : "" %>>Usuario</option>
                    <option value="administrador" <%= "administrador".equalsIgnoreCase(p.getRol() == null ? "" : p.getRol()) ? "selected" : "" %>>Administrador</option>

                </select><br>
                <label for="contrasena">Contraseña:</label>
                <input id="pass" class="form-control" type="password" name="txtContrasena"
                       placeholder="Deja en blanco para mantener la actual"><br>

                <input type="hidden" name="txtid" value="<%= p.getId()%>">
                <input class="btn btn-primary" type="submit" name="accion" value="Actualizar"> 
                <a href="Controlador?accion=listar">Regresar</a>
            </form>
          </div>
          
        </div>
    </body>
</html>
