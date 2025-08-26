<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Persona"%>
<%
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
        <form action="ControladorAdmin" method="get" autocomplete="off">
            <input type="hidden" name="accion" value="Agregar">
            <label>DPI:</label>
            <input class="form-control mb-2" type="text" name="txtDpi" autocomplete="off" required>
            <label>Nombres:</label>
            <input class="form-control mb-2" type="text" name="txtNom" autocomplete="off" required>
            <label>Usuario:</label>
            <input class="form-control mb-2" type="text" name="txtUsuario" autocomplete="off" required>
            <label>Correo:</label>
            <input class="form-control mb-2" type="email" name="txtCorreo" autocomplete="off" required>
            <label>Rol:</label>
            <select class="form-control mb-2" name="txtRol" required>
                <option value="">Seleccione un rol</option>
                <option value="residente">Residente</option>
                <option value="administrador">Administrador</option>
                <option value="seguridad">Seguridad</option>
            </select>
            <label>Lote:</label>
            <input class="form-control mb-2" type="text" name="txtLote">
            <label>No. Casa:</label>
            <input class="form-control mb-2" type="text" name="txtNumeroCasa">
            <label>Estado:</label>
            <select class="form-control mb-2" name="txtEstado" required>
                <option value="activo">Activo</option>
                <option value="inactivo">Inactivo</option>
            </select>
            <label>Contraseña:</label>
            <div class="input-group mb-3">
                <input class="form-control" type="password" name="txtContrasena" autocomplete="new-password" required>
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
