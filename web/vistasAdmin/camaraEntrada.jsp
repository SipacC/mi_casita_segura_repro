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
+<head>
    <meta charset="UTF-8">
    <title>Cámara Entrada</title>
    <script src="https://unpkg.com/html5-qrcode" type="text/javascript"></script>
    <style>
        #reader { width: 100%; max-width: 400px; margin: auto; }
    </style>
</head>
<body>
    <h2>📷 Cámara Entrada</h2>
    <div id="reader"></div>

    <script>
    let host = window.location.hostname; 
    let socket = new WebSocket("ws://" + host + ":8080/CRUD-MVC-JAVA/qrSocket");

    socket.onopen = () => console.log("✅ Conectado a WebSocket (Entrada)");

    socket.onmessage = (event) => {
        let data = JSON.parse(event.data);
        if (data.resultado === "valido") {
            alert("✅ Entrada permitida: " + data.usuario);
        } else if (data.resultado === "invalido") {
            alert("❌ QR inválido en entrada");
            new Audio("<%= request.getContextPath() %>/audio/mostrar_qr.mp3").play();
        } else if (data.resultado === "duplicado") {
            console.log("⚠️ QR duplicado ignorado en cliente");
        } else {
            alert("⚠️ Error en validación del QR");
        }
    };

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
