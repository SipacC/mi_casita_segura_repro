<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr != null) {
        // Redirige según rol
        if ("administrador".equalsIgnoreCase(usr.getRol())) {
            response.sendRedirect(request.getContextPath() + "/vistasAdmin/menuAdministrador.jsp");
        } else {
            response.sendRedirect(request.getContextPath() + "/ControladorAdmin?accion=listar");
        }
        return;
    }
%>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="css/login.css">


<div class="login-container">
    <h2>Iniciar sesion</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message"><%= request.getAttribute("error") %></div>
    <% } %>
    <form action="ControladorLogin" method="post" autocomplete="off">
    <input type="hidden" name="accion" value="validar">
    <input type="text" name="usuario" placeholder="Usuario" required>
    <input type="password" name="contrasena" placeholder="Contraseña" required>
    <button type="submit">Entrar</button>
    </form>
</div>
