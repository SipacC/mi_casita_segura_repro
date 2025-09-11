<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Registrar Pago</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="container mt-4">
    <h2>➕ Registrar Pago Manual</h2>

    <form action="<%= request.getContextPath() %>/ControladorResidente" method="get">
        <input type="hidden" name="accion" value="guardarPago">
        <input type="hidden" name="mora" value="0">

        <div class="mb-3">
            <label for="tipo_pago">Tipo de pago</label>
            <select id="tipo_pago" name="tipo_pago" class="form-control">
                <option value="Mantenimiento">Mantenimiento</option>
                <option value="Multa">Multa</option>
                <option value="Reinstalación de servicios">Reinstalación de servicios</option>
            </select>
        </div>

        <div class="mb-3">
            <label>Monto</label>
            <input type="text" name="monto" class="form-control">
        </div>

        <div class="mb-3">
            <label>Método de pago</label>
            <select name="metodo_pago" class="form-control">
                <option value="Efectivo">Efectivo</option>
                <option value="Tarjeta">Tarjeta</option>
            </select>
        </div>

        <div class="mb-3">
            <label>Observaciones</label>
            <textarea name="observaciones" class="form-control"></textarea>
        </div>

        <button type="submit" class="btn btn-primary">Guardar</button>
        <a href="<%= request.getContextPath() %>/ControladorResidente?accion=pagos" class="btn btn-secondary">⬅ Volver</a>
    </form>
</body>
</html>
