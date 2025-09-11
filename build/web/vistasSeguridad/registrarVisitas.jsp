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
    <title>Registrar Visitas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-5">
    <div class="card p-4 shadow-lg">
        <h2 class="mb-3">Registrar Visita</h2>
        <form action="ControladorSeguridad?accion=guardarVisita" method="post">
            <div class="mb-3">
                <label for="nombre" class="form-label">Nombre de la visita</label>
                <input type="text" class="form-control" id="nombre" name="nombre" required>
            </div>
            <div class="mb-3">
                <label for="dpi" class="form-label">DPI</label>
                <input type="text" class="form-control" id="dpi" name="dpi" required>
            </div>
            <button type="submit" class="btn btn-primary">Registrar</button>
        </form>

        <a class="btn btn-secondary mt-3" href="<%= request.getContextPath() %>/ControladorSeguridad?accion=menu">⬅ Volver al menú</a>
    </div>
</div>
</body>
</html>
