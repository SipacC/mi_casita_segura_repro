<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
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
    <title>Reservas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-4">
    <h2>📅 Reservas de Áreas Comunes</h2>

    <!-- Botón regresar -->
    <a href="<%= request.getContextPath() %>/vistasResidente/menuResidente.jsp" class="btn btn-secondary mb-3">⬅ Volver al menú</a>

    <!-- Placeholder -->
    <div class="alert alert-info">
        Aquí se mostrará el historial de reservas del residente y la opción de crear/cancelar reservas.
    </div>

    <a href="#" class="btn btn-primary">➕ Crear nueva reserva</a>
</div>
</body>
</html>
