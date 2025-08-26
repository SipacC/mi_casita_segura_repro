<%@page import="Modelo.Persona"%>
<%@page import="ModeloDAO.PersonaDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <title>Modificar Persona</title>
</head>
<body>
<div class="container mt-4">
    <div class="col-lg-6 mx-auto">
        <%
            PersonaDAO dao = new PersonaDAO();
            String idStr = (String) request.getAttribute("idper");
            if (idStr == null) { idStr = request.getParameter("id"); }
            int id = Integer.parseInt(idStr);
            Persona p = dao.list(id);
        %>
        <h1 class="mb-3">Modificar Persona</h1>
        <form action="ControladorAdmin" method="get" autocomplete="off">
            <input type="hidden" name="accion" value="Actualizar">
            <input type="hidden" name="txtid" value="<%= p.getId_usuario() %>">

            <label for="dpi">DPI:</label>
            <input id="dpi" class="form-control mb-2" type="text" name="txtDpi" value="<%= p.getDpi() %>" required>

            <label for="nombres">Nombres:</label>
            <input id="nombres" class="form-control mb-2" type="text" name="txtNom" value="<%= p.getNombres() %>" required>

            <label for="usuario">Usuario:</label>
            <input id="usuario" class="form-control mb-2" type="text" name="txtUsuario" value="<%= p.getUsuario() %>" required>

            <label for="correo">Correo:</label>
            <input id="correo" class="form-control mb-2" type="email" name="txtCorreo" value="<%= p.getCorreo() %>" required>

            <label for="rol">Rol:</label>
            <select id="rol" class="form-control mb-2" name="txtRol" required>
                <option value="">Seleccione</option>
                <option value="residente" <%= "residente".equalsIgnoreCase(p.getRol() == null ? "" : p.getRol()) ? "selected" : "" %>>Residente</option>
                <option value="administrador" <%= "administrador".equalsIgnoreCase(p.getRol() == null ? "" : p.getRol()) ? "selected" : "" %>>Administrador</option>
                <option value="seguridad" <%= "seguridad".equalsIgnoreCase(p.getRol() == null ? "" : p.getRol()) ? "selected" : "" %>>Seguridad</option>
            </select>

            <label for="lote">Lote:</label>
            <input id="lote" class="form-control mb-2" type="text" name="txtLote" value="<%= p.getLote() %>">

            <label for="numeroCasa">No. Casa:</label>
            <input id="numeroCasa" class="form-control mb-2" type="text" name="txtNumeroCasa" value="<%= p.getNumero_casa() %>">

            <label for="estado">Estado:</label>
            <select id="estado" class="form-control mb-2" name="txtEstado" required>
                <option value="activo" <%= "activo".equalsIgnoreCase(p.getEstado() == null ? "" : p.getEstado()) ? "selected" : "" %>>Activo</option>
                <option value="inactivo" <%= "inactivo".equalsIgnoreCase(p.getEstado() == null ? "" : p.getEstado()) ? "selected" : "" %>>Inactivo</option>
            </select>

            <label for="contrasena">Contraseña:</label>
            <input id="pass" class="form-control mb-3" type="password" name="txtContrasena" placeholder="Deja en blanco para mantener la actual">

            <button class="btn btn-primary" type="submit">Actualizar</button>
            <a href="ControladorAdmin?accion=listar" class="btn btn-secondary ms-2">Regresar</a>
        </form>
    </div>
</div>
</body>
</html>
