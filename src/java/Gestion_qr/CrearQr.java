package Gestion_qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class CrearQr {

    // Genera un QR con id_usuario y un código único
    public static String generarQR(int idUsuario, String codigo) {
        try {
            // Contenido dentro del QR
            String contenidoQR = "ID:" + idUsuario + ";CODIGO:" + codigo;

            // Carpeta donde guardar los QR
            String carpeta = "C:/qrsProyecto/";
            File dir = new File(carpeta);
            if (!dir.exists()) dir.mkdirs();

            // Nombre del archivo
            String nombreArchivo = "qr_usuario_" + idUsuario + ".png";
            Path path = FileSystems.getDefault().getPath(carpeta + nombreArchivo);

            // Generar la matriz QR
            BitMatrix matrix = new MultiFormatWriter().encode(
                contenidoQR, BarcodeFormat.QR_CODE, 300, 300
            );

            // Guardar en disco como PNG
            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            System.out.println("QR generado en: " + path.toAbsolutePath());

            return path.toString(); // Retorna la ruta absoluta
        } catch (Exception e) {
            System.err.println("Error al generar QR: " + e.getMessage());
            return null;
        }
    }
}

