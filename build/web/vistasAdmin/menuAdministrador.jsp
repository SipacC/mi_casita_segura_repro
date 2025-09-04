<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    // ===============================
    // Evitar que el navegador guarde esta página en caché
    // Así, la flecha "Atrás" siempre recarga desde el servidor
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies
    // ===============================

    // ===============================
    // Validar sesión y rol
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr == null || !"administrador".equalsIgnoreCase(usr.getRol())) {
        // Si no hay sesión o el rol no es administrador, redirige al login
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return; // detener ejecución del JSP
    }
    // ===============================
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Menú Administrador</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/admin.css">
</head>
<body>

<div class="admin-container">
    <h2>Menú del Administrador</h2>
    <p>Bienvenido Administrador, <strong><%= usr.getNombres() %></strong></p>

    <!-- Opciones del menú -->
    <div class="list-group">
        <a class="list-group-item" href="<%= request.getContextPath() %>/ControladorAdmin?accion=listar">
            Mantenimiento de usuarios
        </a>
        <a class="list-group-item" href="#">
            Otra opción
        </a>
        <!-- Nueva opción: Administrador de Cámaras -->
        <a class="list-group-item" href="<%= request.getContextPath() %>/vistasAdmin/menuCamaras.jsp">
            Administrador de Cámaras
        </a>
    </div>

    <!-- Botón de Cerrar Sesión -->
    <!-- ===============================
         Llama al servlet ControladorLogin con accion=logout
         Esto destruye la sesión y redirige al login
    =============================== -->
    <a class="logout-btn" href="<%= request.getContextPath() %>/ControladorLogin?accion=logout">Cerrar sesión</a>
</div>

</body>
</html>
