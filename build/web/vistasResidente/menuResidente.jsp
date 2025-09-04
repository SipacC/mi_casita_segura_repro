<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);

    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr == null || !"residente".equalsIgnoreCase(usr.getRol())) {
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú Residente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/residente.css">
</head>
<body>
<div class="menu-container">
    <h2>Menú del Residente</h2>
    <p>Bienvenido <strong><%= usr.getNombres() %></strong></p>

    <div class="list-group">
        <a class="list-group-item" href="#">Gestionar pagos</a>
        <a class="list-group-item" href="#">Mis notificaciones</a>
    </div>

    <a class="logout-btn" href="<%= request.getContextPath() %>/ControladorLogin?accion=logout">Cerrar sesión</a>
</div>
</body>
</html>
