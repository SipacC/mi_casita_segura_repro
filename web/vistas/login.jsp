<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
  <title>Iniciar sesión</title>
</head>
<body>
<div class="container" style="max-width:420px; margin-top:60px;">
  <h2 class="mb-4">Iniciar sesión</h2>

  <% String err = (String) request.getAttribute("error"); %>
  <% if (err != null) { %>
    <div class="alert alert-danger"><%= err %></div>
  <% } %>

  <form action="Controlador" method="get" autocomplete="off">
    <input type="hidden" name="accion" value="validar">

    <div class="mb-3">
      <label for="nom" class="form-label">Nombre</label>
      <input id="nom" name="nom" class="form-control" required>
    </div>

    <div class="mb-3">
      <label for="contrasena" class="form-label">Contraseña</label>
      <input id="contrasena" name="contrasena" type="password" class="form-control" required autocomplete="current-password">
    </div>

    <div class="mb-3">
      <label for="rol" class="form-label">Rol</label>
      <select id="rol" name="rol" class="form-control" required>
        <option value="">Seleccione...</option>
        <option value="usuario">Usuario</option>
        <option value="administrador">Administrador</option>
      </select>
    </div>

    <button class="btn btn-primary" type="submit">Entrar</button>
  </form>
</div>
</body>
</html>
