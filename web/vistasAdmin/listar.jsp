<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Persona"%>

<%
    // ===============================
    // Validar sesión activa
    // ===============================
    Persona usr = (Persona) session.getAttribute("usuario");
    if (usr == null) {
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return; // detener ejecución
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Listado de Personas</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="container mt-4">
    <h1>Personas</h1>
    
    <!-- Botones de acción -->
    <div class="mb-3">
        <a class="btn btn-success" href="ControladorAdmin?accion=add">Agregar Nuevo</a>
        <a class="btn btn-primary" href="<%= request.getContextPath() %>/vistasAdmin/menuAdministrador.jsp">Regresar al Menú</a>
    </div>
    
    <!-- Tabla de personas -->
    <table class="table table-bordered table-striped w-100" style="font-size: 1.1rem;">
        <thead class="table-dark text-center">
            <tr>
                <th>ID</th>
                <th>DPI</th>
                <th>Nombres</th>
                <th>Usuario</th>
                <th>Correo</th>
                <th>Rol</th>
                <th>Contraseña</th>
                <th>Lote</th>
                <th>No. Casa</th>
                <th>Estado</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
        <%
            List<Persona> list = (List<Persona>) request.getAttribute("lista");
            if (list == null || list.isEmpty()) {
        %>
            <tr>
                <td colspan="11" class="text-center text-danger">No hay registros en la base de datos.</td>
            </tr>
        <%
            } else {
                for (Persona per : list) {
        %>
            <tr>
                <td class="text-center"><%= per.getId_usuario() %></td>
                <td class="text-center"><%= per.getDpi() %></td>
                <td><%= per.getNombres() %></td>
                <td><%= per.getUsuario() %></td>
                <td><%= per.getCorreo() %></td>
                <td class="text-center"><%= per.getRol() %></td>
                <td class="text-center"><%= per.getContrasena() %></td>
                <td><%= per.getLote() %></td>
                <td><%= per.getNumero_casa() %></td>
                <td><%= per.getEstado() %></td>
                <td class="text-center">
                    <a class="btn btn-warning" href="ControladorAdmin?accion=editar&id=<%= per.getId_usuario() %>">Editar</a>
                    <a class="btn btn-danger" href="ControladorAdmin?accion=eliminar&id=<%= per.getId_usuario() %>">Eliminar</a>
                </td>
            </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
