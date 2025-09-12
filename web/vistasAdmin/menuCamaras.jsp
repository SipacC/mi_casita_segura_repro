<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr == null || !"administrador".equalsIgnoreCase(usr.getRol())) {
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Administrador de Cámaras</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="admin-container">
    <h2>Administrador de Cámaras</h2>
    <div class="list-group">
        <a class="list-group-item list-group-item-action" 
   href="<%= request.getContextPath() %>/vistasAdmin/camaraEntrada.jsp">
    📷 Cámara Entrada (Teléfono 1)
</a>

<a class="list-group-item list-group-item-action" 
   href="<%= request.getContextPath() %>/vistasAdmin/camaraSalida.jsp">
    📷 Cámara Salida (Teléfono 2)
</a>
    </div>
    <br>
    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/vistasAdmin/menuAdministrador.jsp">
        Volver al Menú Administrador
    </a>

</div>
</body>
</html>
