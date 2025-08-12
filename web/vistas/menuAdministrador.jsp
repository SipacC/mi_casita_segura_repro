<%
  Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
  // Recupera de la sesión el usuario logueado

  if (usr == null || !"administrador".equalsIgnoreCase(usr.getRol())) {
      // Si no hay usuario o el rol no es administrador, redirige al login
      response.sendRedirect(request.getContextPath() + "/Controlador?accion=login");
      return;
  }
%>
<link rel="stylesheet" href="../css/admin.css">


<div class="admin-container">
    <h2>Menu del Administrador</h2>
    <p>Bienvenido Adminstrador, <strong><%= usr.getNom() %></strong></p>

    <div class="list-group">
        <a class="list-group-item" href="../Controlador?accion=listar">
            Mantenimiento de usuarios
        </a>
        <a class="list-group-item" href="#">
            Otra opcion
        </a>
    </div>

    <a class="logout-btn" href="../Controlador?accion=logout">Cerrar sesión</a>
</div>
