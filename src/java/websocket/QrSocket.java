package websocket;

import javax.websocket.*;// Importa las clases principales para trabajar con WebSockets en Java
import javax.websocket.server.ServerEndpoint; // Permite declarar un endpoint de WebSocket en el servidor usando anotaciones.


import Config.Conexion; //importa la conexion 
import java.io.IOException;// Importa la clase para manejar excepciones
import java.sql.*;// Importa todas las clases necesarias para trabajar con JDBC

@ServerEndpoint("/qrSocket")// Anotación que expone esta clase como endpoint de WebSocket accesible en la ruta /qrSocket.
public class QrSocket { // se declara la clase

    private static String ultimoQR = "";// Variable estática que guarda el último código QR recibido, evita procesar duplicados.
    private static long tiempoUltimoQR = 0;// Guarda el tiempo en milisegundos de la última lectura, usado para control de spam., lo de duplicados

    @OnOpen // Anotación que indica que el método se ejecuta cuando un cliente abre una conexión WebSocket.

    public void onOpen(Session session) {
        // Método que recibe la sesión del cliente que se conecta.
        System.out.println("Conexión WebSocket abierta: " + session.getId());
        try {
            ArduinoSerial.conectar("COM3", 115200);
            //abre la conexion serail con el arduino, en puerto COM3, a 115200 baudios
        } catch (Exception e) {
            System.err.println("Error al conectar con Arduino: " + e.getMessage());
            // encaso de haber un error lo imprime en consola, error al conctar con el arduino
        }
    }

    @OnMessage // Anotación: este método se ejecuta cada vez que se recibe un mensaje del cliente WebSocket.
    public void onMessage(String message, Session session) throws IOException {
        // Recibe el mensaje como String y la sesión que lo envió.
        System.out.println("QR recibido: " + message);// Imprime en consola el QR recibido.
        String respuesta = validarQr(message);// Llama al método validarQr para procesar y validar el código QR.
        session.getBasicRemote().sendText(respuesta); // Envía la respuesta en formato JSON al cliente conectado.
    }

    @OnClose // Anotación: indica que el método se ejecuta al cerrar la conexión WebSocket.
    public void onClose(Session session) {
        System.out.println("🔌 Conexión WebSocket cerrada: " + session.getId());// Imprime en consola que la conexión fue cerrada, con el ID de la sesión.
        try {
            ArduinoSerial.cerrar();// Cierra la conexión con el Arduino.
        } catch (Exception e) {
            System.err.println("Error al cerrar conexión con Arduino: " + e.getMessage());// Si ocurre error, lo imprime.
        }
    }

    @OnError // Anotación: indica que este método se ejecuta si ocurre un error en la conexión WebSocket.
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error en WebSocket: " + throwable.getMessage());// Imprime el mensaje del error
        throwable.printStackTrace();// Muestra toda la traza del error para depuración, permite seguir el “camino” del error y encontrar exactamente dónde y por qué ocurrió.
    }

    private String validarQr(String message) {// Método privado que recibe el QR y lo valida en la base de datos.
        long ahora = System.currentTimeMillis();// Obtiene el tiempo actual en milisegundos
        if (message.equals(ultimoQR) && (ahora - tiempoUltimoQR < 2000)) {// Si el mensaje es igual al último y fue recibido hace menos de 2 segundos...
            System.out.println("Ignorado QR duplicado en servidor (anti-spam): " + message);// Se ignora el QR (anti-spam).
            return "{\"resultado\":\"duplicado\"}";// Devuelve JSON indicando duplicado.
        }
        ultimoQR = message;// Actualiza el último QR recibido.
        tiempoUltimoQR = ahora; // Guarda el tiempo en que llegó.

        Conexion cn = new Conexion();// Crea un objeto de la clase Conexion (propia) para conectarse a la BD, ya qeu la conexion no es dinamica
        try (Connection con = cn.getConnection()) { // Abre conexión JDBC .

            String[] partes = message.split(";");// Divide el mensaje en partes separadas por ";".
            String tipo = "entrada"; // Por defecto, se considera que el QR es de tipo "entrada".
            String idStr;  // Declaración para almacenar id de usuario.
            String codigo; // Declaración para almacenar el código QR.

            if (message.startsWith("tipo:")) {
                 // Si el mensaje contiene explícitamente el tipo (ej. tipo:entrada;id:1;codigo:123).
                tipo = partes[0].split(":")[1]; // Extrae el tipo después de ":".
                idStr = partes[1].split(":")[1]; // Extrae el ID del usuario.
                codigo = partes[2].split(":")[1]; // Extrae el código QR.
            } else {
                idStr = partes[0].split(":")[1]; // Si el mensaje no trae tipo explícito.
                codigo = partes[1].split(":")[1];// Toma el código QR.
            }

            int idUsuario = Integer.parseInt(idStr);// Convierte el id a entero.

            PreparedStatement ps = con.prepareStatement(
                "SELECT q.id_qr_usuario, u.nombres " +
                "FROM qr_usuario q " +
                "INNER JOIN usuarios u ON q.id_usuario = u.id_usuario " +
                "WHERE q.id_usuario=? AND q.codigo_qr_usuario=? AND q.estado='activo'"// Prepara consulta SQL parametrizada para buscar si el QR es válido y está activo.
            );
            ps.setInt(1, idUsuario);// Inserta el parámetro ID en la consulta.
            ps.setString(2, codigo);// Inserta el código QR en la consulta.
            ResultSet rs = ps.executeQuery(); // Ejecuta la consulta y obtiene los resultados.

            if (rs.next()) { // Si existe un resultado en la BD → QR válido.
                int idQrUsuario = rs.getInt("id_qr_usuario");// Obtiene el ID del QR.
                String usuario = rs.getString("nombres"); // Obtiene el nombre del usuario.

                PreparedStatement psUltimo = con.prepareStatement(
                    "SELECT tipo FROM acceso_usuario WHERE id_qr_usuario=? ORDER BY fecha_hora DESC LIMIT 1" // Consulta el último acceso registrado de ese usuario.
                );
                psUltimo.setInt(1, idQrUsuario);// Inserta el ID de QR en la consulta.
                ResultSet rsUltimo = psUltimo.executeQuery(); // Ejecuta la consulta.

                if (rsUltimo.next()) {// Si hay registros previos de accesos.
                    String ultimoTipo = rsUltimo.getString("tipo"); // Obtiene el último tipo (entrada/salida).

                    if ("entrada".equalsIgnoreCase(tipo) && "entrada".equalsIgnoreCase(ultimoTipo)) {
                        System.out.println("Usuario ya estaba dentro → no puede volver a entrar.");// Si intenta entrar dos veces seguidas.
                        return "{\"resultado\":\"duplicado\",\"usuario\":\"" + usuario + "\"}"; // Devuelve JSON indicando duplicado.
                    }

                    if ("salida".equalsIgnoreCase(tipo) && "salida".equalsIgnoreCase(ultimoTipo)) {
                        System.out.println("Usuario ya estaba fuera → no puede volver a salir.");// Si intenta salir dos veces seguidas.
                        return "{\"resultado\":\"duplicado\",\"usuario\":\"" + usuario + "\"}";
                    }
                }

                
                PreparedStatement ps2 = con.prepareStatement(
                    "INSERT INTO acceso_usuario (id_qr_usuario, tipo, fecha_hora) VALUES (?, ?, NOW())"
                );// Inserta nuevo acceso válido en la BD.
                ps2.setInt(1, idQrUsuario); // Inserta ID del QR.
                ps2.setString(2, tipo);// Inserta el tipo (entrada/salida).
                ps2.executeUpdate();// Ejecuta el INSERT en la base de datos.

                if ("entrada".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("1"); // Si es entrada, envía señal "1" al Arduino.
                } else if ("salida".equalsIgnoreCase(tipo)) {
                    ArduinoSerial.enviar("2");// Si es salida, envía señal "2".
                }

                return "{\"resultado\":\"valido\",\"usuario\":\"" + usuario + "\"}"; // Devuelve JSON indicando válido.
            } else {
                return "{\"resultado\":\"invalido\"}"; // Si no existe en la BD, el QR es inválido
            }

        } catch (Exception e) {
            e.printStackTrace();// Si ocurre algún error lo imprime.
            return "{\"resultado\":\"error\"}"; // Devuelve JSON con error
        }
    }
}

