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
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Menú Residente</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/residente.css">
</head>
<body>
<div class="container mt-4">
    <div class="card shadow-lg p-4">
        <h2 class="text-center">🏠 Menú del Residente</h2>
        <p class="text-center">Bienvenido <strong><%= usr.getNombres() %></strong></p>

        <div class="list-group">
            <!-- Opciones habilitadas -->
            <a class="list-group-item list-group-item-action" 
               href="<%= request.getContextPath() %>/ControladorResidente?accion=pagos">
                💳 Gestionar pagos
            </a>
            <a class="list-group-item list-group-item-action" 
               href="<%= request.getContextPath() %>/ControladorResidente?accion=reservas">
                📅 Gestionar reservas
            </a>
        </div>

        <!-- Botón logout -->
        <div class="text-center mt-3">
            <a class="btn btn-danger" 
               href="<%= request.getContextPath() %>/ControladorLogin?accion=logout">
                Cerrar sesión
            </a>
        </div>
    </div>
</div>
</body>
</html>

