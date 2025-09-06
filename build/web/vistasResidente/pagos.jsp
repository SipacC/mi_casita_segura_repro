<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Pago"%>
<%
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    List<Pago> lista = (List<Pago>) request.getAttribute("listaPagos");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Pagos Realizados</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <h2>💳 Pagos de <%= usr.getNombres() %></h2>

    <table class="table table-bordered table-hover">
        <thead class="table-dark text-center">
            <tr>
                <th>ID</th>
                <th>Tipo</th>
                <th>Método</th>
                <th>Monto</th>
                <th>Mora</th>
                <th>Estado</th>
                <th>Fecha</th>
            </tr>
        </thead>
        <tbody>
        <%
            if (lista == null || lista.isEmpty()) {
        %>
            <tr>
                <td colspan="7" class="text-center text-danger">No hay pagos registrados.</td>
            </tr>
        <%
            } else {
                for (Pago p : lista) {
        %>
            <tr>
                <td><%= p.getId_pago() %></td>
                <td><%= p.getId_tipo() %></td>
                <td><%= p.getMetodo_pago() %></td>
                <td>Q <%= p.getMonto() %></td>
                <td>Q <%= p.getMora() %></td>
                <td><%= p.getEstado() %></td>
                <td><%= p.getFecha_pago() %></td>
            </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>

    <!-- Botón consultar pagos pendientes -->
    <a href="<%= request.getContextPath() %>/ControladorResidente?accion=consultarPago" 
       class="btn btn-primary">🔎 Consultar pagos pendientes</a>

    <a href="<%= request.getContextPath() %>/vistasResidente/menuResidente.jsp" 
       class="btn btn-secondary">⬅ Volver al menú</a>
</body>
</html>
