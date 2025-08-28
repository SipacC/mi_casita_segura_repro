package arduino;

import com.fazecast.jSerialComm.SerialPort;

public class ArduinoSerial {
    private static SerialPort arduinoPort;

    // Abrir conexión
    public static void conectar(String puerto, int baudRate) {
        arduinoPort = SerialPort.getCommPort(puerto);
        arduinoPort.setComPortParameters(baudRate, 8, 1, 0);
        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);

        if (arduinoPort.openPort()) {
            System.out.println("✅ Conectado al Arduino en " + puerto);
        } else {
            System.out.println("❌ Error al conectar con Arduino");
        }
    }

    // Enviar comando
    public static void enviar(String comando) {
        try {
            if (arduinoPort != null && arduinoPort.isOpen()) {
                arduinoPort.getOutputStream().write((comando + "\n").getBytes());
                arduinoPort.getOutputStream().flush();
                System.out.println("📤 Comando enviado: " + comando);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Cerrar puerto
    public static void cerrar() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            arduinoPort.closePort();
        }
    }
}
