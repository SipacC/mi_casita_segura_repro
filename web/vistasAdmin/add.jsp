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
            <div class="col-lg-6">
                <h1>Agregar Persona</h1>
                <form action="ControladorAdmin" method="get" autocomplete="off">
                    <label> DPI:</label>
                    <input class="form-control" type="text" name="txtDpi"
                    autocomplete="off"
                    value="" required><br>
                    <label>Nombres:</label> 
                    <input class="form-control" type="text" name="txtNom"
                    autocomplete="off"
                    value="" required><br>
                    <label>Rol:</label>
                    <select id="rol" class = "form-control" name = "txtRol" required>
                        <option value="">Seleccione un rol</option>
                        <option value="usuario">Usuario</option>
                        <option value="administrador">Administrador</option>
                    </select><br>
                    <label>Contraseña:</label>
                    <div class="input-group">
                        <input class="form-control" type="password" name="txtContrasena"
                               autocomplete="new-password"
                               value="" required>
                        <button type="button" class="btn btn-outline-secudary"
                        onclick="const i=this.previousElementSibling; i.type=i.type==='password'?'text':'password'; this.innerText=i.type==='password'?'Mostrar':'Ocultar'">
                    Mostrar
                        </button>
                    </div>
                    <br>
                    <input class="btn btn-primary" type="submit" name="accion" value="Agregar">
                    <a href="ControladorAdmin?accion=listar">Regresar</a>
                </form>
            </div>

        </div>
    </body>
</html>
