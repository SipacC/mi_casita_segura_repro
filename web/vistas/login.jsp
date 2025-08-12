<form action="Controlador" method="post" autocomplete="off">
  <!-- Envia los datos al servlet Controlador usando POST -->
  <input type="hidden" name="accion" value="validar">
  <!-- Campo oculto que indica al servlet qué acción ejecutar -->

  <div class="mb-3">
    <label for="nom" class="form-label">Nombre</label>
    <input id="nom" name="nom" class="form-control" required>
    <!-- Campo de texto para ingresar el nombre de usuario -->
  </div>

  <div class="mb-3">
    <label for="contrasena" class="form-label">Contraseña</label>
    <input id="contrasena" name="contrasena" type="password" class="form-control" required autocomplete="current-password">
    <!-- Campo de contraseña -->
  </div>

  <button class="btn btn-primary" type="submit">Entrar</button>
  <!-- Botón para enviar el formulario -->
</form>
