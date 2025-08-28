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
    <title>Cámara Salida</title>
    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
    <style>
        #reader { width: 100%; max-width: 400px; margin: auto; }
    </style>
</head>
<body>
    <h2>📷 Cámara Salida</h2>
    <div id="reader"></div>

    <script>
        let socket = new WebSocket("ws://192.168.1.9:8080/CRUD-MVC-JAVA/qrSocket");

        socket.onopen = () => console.log("✅ Conectado a WebSocket (Salida)");

        socket.onmessage = (event) => {
            let data = JSON.parse(event.data);
            if (data.resultado === "valido") {
                alert("✅ Salida permitida: " + data.usuario);
            } else if (data.resultado === "invalido") {
                alert("❌ QR inválido en salida");
                new Audio("<%= request.getContextPath() %>/audio/mostrar_qr.mp3").play();
            } else {
                alert("⚠️ Error en validación del QR");
            }
        };

        socket.onerror = (error) => console.error("⚠️ Error WebSocket:", error);
        socket.onclose = () => console.log("❌ WebSocket cerrado");

        function onScanSuccess(decodedText, decodedResult) {
            console.log(`QR detectado: ${decodedText}`);
            // Mandamos al servidor con tipo=salida
            socket.send("tipo:salida;" + decodedText);
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
