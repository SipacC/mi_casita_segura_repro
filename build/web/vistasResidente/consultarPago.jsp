<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    String tipoPago = (String) request.getAttribute("tipoPago");
    Double totalPago = (Double) request.getAttribute("totalPago");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Consultar Pagos</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script>
        function toggleTarjeta() {
            const metodo = document.getElementById("metodo_pago").value;
            document.getElementById("tarjetaFields").style.display = (metodo === "Tarjeta") ? "block" : "none";
        }
    </script>
</head>
<body class="container mt-4">
    <h2>🔎 Consultar Pagos Pendientes</h2>
    <p><strong>Residente:</strong> <%= usr.getNombres() %></p>

    <!-- 🔹 Formulario inicial para elegir tipo de pago -->
    <form action="<%= request.getContextPath() %>/ControladorResidente" method="get" class="mb-4">
        <input type="hidden" name="accion" value="consultarPago">

        <div class="mb-3">
            <label for="tipo_pago">Seleccione el tipo de pago:</label>
            <select name="tipo_pago" id="tipo_pago" class="form-control" required>
                <option value="">-- Seleccione --</option>
                <option value="Mantenimiento" <%= "Mantenimiento".equals(tipoPago) ? "selected" : "" %>>Mantenimiento</option>
                <option value="Multa" <%= "Multa".equals(tipoPago) ? "selected" : "" %>>Multa</option>
                <option value="Reinstalación de servicios" <%= "Reinstalación de servicios".equals(tipoPago) ? "selected" : "" %>>Reinstalación de servicios</option>
            </select>
        </div>

        <button type="submit" class="btn btn-primary">🔍 Consultar</button>
        <a href="<%= request.getContextPath() %>/ControladorResidente?accion=pagos" 
           class="btn btn-secondary">⬅ Volver</a>
    </form>

    <% if (tipoPago != null && totalPago != null && totalPago > 0) { %>
        <div class="alert alert-info">
            <p>Tipo de pago: <strong><%= tipoPago %></strong></p>
            <p>Monto total (con mora si aplica): <strong>Q <%= totalPago %></strong></p>
        </div>

        <!-- 🔹 Formulario de pago -->
        <form action="<%= request.getContextPath() %>/ControladorResidente" method="get">
            <input type="hidden" name="accion" value="guardarPago">
            <input type="hidden" name="tipo_pago" value="<%= tipoPago %>">
            <input type="hidden" name="monto" value="<%= totalPago %>">
            <input type="hidden" name="mora" value="0">

            <div class="mb-3">
                <label for="metodo_pago">Método de pago</label>
                <select id="metodo_pago" name="metodo_pago" class="form-control" onchange="toggleTarjeta()">
                    <option value="Efectivo">Efectivo</option>
                    <option value="Tarjeta">Tarjeta</option>
                </select>
            </div>

            <!-- 🔹 Campos de tarjeta -->
            <div id="tarjetaFields" style="display:none;">
                <div class="mb-3">
                    <label>Número tarjeta</label>
                    <input type="text" name="numero" class="form-control">
                </div>
                <div class="mb-3">
                    <label>Nombre titular</label>
                    <input type="text" name="nombre" class="form-control">
                </div>
                <div class="mb-3">
                    <label>Expira (MM/AA)</label>
                    <input type="text" name="expira" class="form-control">
                </div>
                <div class="mb-3">
                    <label>CVV</label>
                    <input type="password" name="cvv" class="form-control">
                </div>
            </div>

            <div class="mb-3">
                <label>Observaciones</label>
                <textarea name="observaciones" class="form-control"></textarea>
            </div>

            <button type="submit" class="btn btn-success">💳 Realizar pago</button>
        </form>
    <% } else if (tipoPago != null) { %>
        <div class="alert alert-warning">⚠️ No tienes pagos pendientes de tipo <%= tipoPago %>.</div>
    <% } %>
</body>
</html>
