<%
  Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
  // Recupera de la sesión el usuario logueado

  if (usr == null || !"administrador".equalsIgnoreCase(usr.getRol())) {
      // Si no hay usuario o el rol no es administrador, redirige al login
      response.sendRedirect(request.getContextPath() + "/Controlador?accion=login");
      return;
  }
%>

<p>Bienvenido, <strong><%= usr.getNom() %></strong></p>
<!-- Muestra el nombre del usuario logueado -->

<a class="list-group-item list-group-item-action" href="../Controlador?accion=listar">
  Mantenimiento de usuarios
</a>
<!-- Enlace para ir a la lista de usuarios (listar.jsp) -->

<a class="btn btn-outline-danger" href="../Controlador?accion=logout">Cerrar sesión</a>
<!-- Enlace para cerrar sesión -->
