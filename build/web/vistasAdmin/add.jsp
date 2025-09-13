<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Persona"%>
<%
    // Verificación de sesión
    Modelo.Persona usr = (Modelo.Persona) session.getAttribute("usuario");
    if (usr == null) {
        response.sendRedirect(request.getContextPath() + "/ControladorLogin?accion=login");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Agregar Persona</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<div class="container mt-4">
    <div class="col-lg-6 mx-auto">

        <h1 class="mb-3">Agregar Persona</h1>
        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger text-center" role="alert">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="ControladorAdmin" method="get" autocomplete="off">
            <input type="hidden" name="accion" value="Agregar">

            <label for="dpi">DPI:</label>
            <input id="dpi" class="form-control mb-2" type="text" name="txtDpi" required
            maxlength="13" 
            pattern="\d{13}" 
            title="El DPI debe tener exactamente 13 dígitos numéricos">

            <label for="nombres">Nombres:</label>
            <input id="nombres" class="form-control mb-2" type="text" name="txtNombres" required>

            <label for="usuario">Usuario:</label>
            <input id="usuario" class="form-control mb-2" type="text" name="txtUsuario" required>

            <label for="correo">Correo:</label>
            <input id="correo" class="form-control mb-2" type="email" name="txtCorreo" required>

            <label for="rol">Rol:</label>
            <select id="rol" class="form-control mb-2" name="txtRol" required>
                <option value="">Seleccione un rol</option>
                <option value="residente">Residente</option>
                <option value="administrador">Administrador</option>
                <option value="seguridad">Seguridad</option>
            </select>

            <label for="lote">Lote:</label>
            <input id="lote" class="form-control mb-2" type="text" name="txtLote">

            <label for="numeroCasa">No. Casa:</label>
            <input id="numeroCasa" class="form-control mb-2" type="text" name="txtNumeroCasa">

            <label for="estado">Estado:</label>
            <select id="estado" class="form-control mb-2" name="txtEstado" required>
                <option value="activo">Activo</option>
                <option value="inactivo">Inactivo</option>
            </select>

            <label for="contrasena">Contraseña:</label>
            <div class="input-group mb-3">
                <input id="contrasena" class="form-control" type="password" name="txtContrasena" autocomplete="new-password" required>
                <button type="button" class="btn btn-outline-secondary"
                        onclick="const i=this.previousElementSibling;i.type=i.type==='password'?'text':'password';this.innerText=i.type==='password'?'Mostrar':'Ocultar'">
                    Mostrar
                </button>
            </div>

            <div class="d-flex">
                <button class="btn btn-primary" type="submit">Agregar</button>
                <a href="ControladorAdmin?accion=listar" class="btn btn-secondary ms-2">Regresar</a>
            </div>
        </form>
    </div>
</div>
</body>
</html>
