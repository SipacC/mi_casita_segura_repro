<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr == null || !"seguridad".equalsIgnoreCase(usr.getRol())) {
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Control de Accesos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card p-4 shadow-lg">
        <h2 class="mb-3">Control de Accesos</h2>
        <p>Aquí se mostrará el registro de entradas y salidas en tiempo real o histórico.</p>

        <a class="btn btn-secondary mt-3" href="<%= request.getContextPath() %>/ControladorSeguridad?accion=menu">⬅ Volver al menú</a>
    </div>
</div>
</body>
</html>
