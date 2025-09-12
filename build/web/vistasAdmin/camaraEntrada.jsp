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
    <title>Cámara Entrada</title>
    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
    <style>
        #reader { width: 100%; max-width: 400px; margin: auto; }
        #mensajes { margin-top: 20px; font-size: 1.2em; text-align: center; }
    </style>
</head>
<body>
    <h2>📷 Cámara Entrada</h2>
    <div id="reader"></div>
    <div id="mensajes"></div> <!-- Aquí mostramos mensajes -->

    <script>
    let host = window.location.hostname; 
    let socket = new WebSocket("ws://" + host + ":8080/CRUD-MVC-JAVA/qrSocket");

    socket.onopen = () => {
        console.log("✅ Conectado a WebSocket (Entrada)");
        document.getElementById("mensajes").innerText = "Conectado al servidor ✔️";
    };

    socket.onerror = (event) => {
        console.error("❌ Error en WebSocket", event);
        document.getElementById("mensajes").innerText = "❌ Error en conexión WebSocket";
    };

    socket.onclose = () => {
        console.warn("⚠️ Conexión WebSocket cerrada");
        document.getElementById("mensajes").innerText = "⚠️ Conexión cerrada con el servidor";
    };

    socket.onmessage = (event) => {
        let data = JSON.parse(event.data);
        let mensajeDiv = document.getElementById("mensajes");

        if (data.resultado === "valido") {
            alert("✅ Entrada permitida: " + data.usuario);
            mensajeDiv.innerText = "✅ Bienvenido " + data.usuario;

        } else if (data.resultado === "invalido") {
            alert("❌ QR inválido en entrada");
            mensajeDiv.innerText = "❌ QR inválido o desactivado";
            new Audio("<%= request.getContextPath() %>/audio/mostrar_qr.mp3").play();

        } else if (data.resultado === "duplicado") {
            alert("⚠️ Ya estaba dentro/fuera");
            mensajeDiv.innerText = "⚠️ Acceso duplicado";

        } else if (data.resultado === "error") {
            if (data.detalle === "conexion_bd") {
                alert("💥 Error de conexión a la base de datos");
                mensajeDiv.innerText = "💥 Error de conexión a la base de datos";
            } else if (data.detalle === "sql_error") {
                alert("⚠️ Error en la consulta SQL");
                mensajeDiv.innerText = "⚠️ Error en la consulta SQL";
            } else if (data.detalle === "formato_mensaje") {
                alert("⚠️ Formato de QR inválido");
                mensajeDiv.innerText = "⚠️ Formato de QR inválido";
            } else {
                alert("⚠️ Error inesperado en servidor");
                mensajeDiv.innerText = "⚠️ Error inesperado en servidor";
            }

        } else {
            alert("⚠️ Error desconocido en validación del QR");
            mensajeDiv.innerText = "⚠️ Error desconocido";
        }
    };

    // --- Control de escaneo ---
    let ultimoQR = "";
    let tiempoUltimo = 0;

    function onScanSuccess(decodedText, decodedResult) {
        let ahora = Date.now();
        if (decodedText === ultimoQR && (ahora - tiempoUltimo) < 2000) {
            console.log("⚠️ QR repetido ignorado en cliente:", decodedText);
            return;
        }
        ultimoQR = decodedText;
        tiempoUltimo = ahora;

        console.log(`QR detectado: ${decodedText}`);
        socket.send("tipo:entrada;" + decodedText);
    }

    let html5QrcodeScanner = new Html5QrcodeScanner("reader", { fps: 10, qrbox: 250 });
    html5QrcodeScanner.render(onScanSuccess);
</script>

    <br>
    <a class="btn btn-secondary" href="<%= request.getContextPath() %>/vistasAdmin/menuCamaras.jsp">
        ⬅️ Volver al Menú de Cámaras
    </a>
</body>
</html>
