<link rel="stylesheet" href="css/login.css">


<div class="login-container">
    <h2>Iniciar sesion</h2>

    <% if (request.getAttribute("error") != null) { %>
        <div class="error-message"><%= request.getAttribute("error") %></div>
    <% } %>

    <form action="Controlador" method="post" autocomplete="off">
        <input type="hidden" name="accion" value="validar">
        <input type="text" name="nom" placeholder="Nombre de usuario" required>
        <input type="password" name="contrasena" placeholder="Contrasena" required>
        <button type="submit">Entrar</button>
    </form>
</div>
