<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="../css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <title>Iniciar Sesión</title>
  </head>
  <body>
    <div class="container mt-5" style="max-width:480px;">
      <h2 class="mb-4">Iniciar Sesión</h2>
      <form action="../Controlador" method="post" autocomplete="off">
        <input type="hidden" name="accion" value="Ingresar"/>

        <div class="mb-3">
          <label class="form-label">DPI</label>
          <input class="form-control" type="text" name="txtDpi" required/>
        </div>

        <div class="mb-3">
          <label class="form-label">Contraseña</label>
          <input class="form-control" type="password" name="txtContra" required/>
        </div>

        <button class="btn btn-primary w-100" type="submit">Ingresar</button>

        <p class="mt-3 text-danger">
          <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
        </p>
      </form>
    </div>
  </body>
</html>
