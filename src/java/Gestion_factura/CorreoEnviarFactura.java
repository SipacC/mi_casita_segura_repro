package Gestion_factura;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;
import java.io.File;

public class CorreoEnviarFactura {

    private final String remitente = "sipacchuquiejj@gmail.com";   // ⚠️ tu correo
    private final String password = "fzdp wmxq aixb puxo";        // ⚠️ App Password (no tu clave normal)

    /**
     * Envía un correo con la factura adjunta en PDF
     * @param destinatario correo del residente
     * @param nombreUsuario nombre del residente
     * @param rutaFactura ruta absoluta al PDF generado
     * @param detallePago descripción del pago (ej: "Pago en efectivo (Mantenimiento)")
     */
    public boolean enviarFactura(String destinatario, String nombreUsuario, String rutaFactura, String detallePago) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remitente, "Sistema de Facturación - Mi Casita Segura"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject("Factura generada: " + detallePago);

            // 📌 Cuerpo del mensaje
            String cuerpo = "¡Hola " + nombreUsuario + "!\n\n"
                    + "Se ha realizado con éxito el " + detallePago + ".\n"
                    + "Adjunto encontrará su factura en formato PDF.\n\n"
                    + "Gracias por confiar en el residencial Mi Casita Segura.\n";

            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo, "utf-8");

            // 📌 Adjuntar PDF
            File archivo = new File(rutaFactura);
            if (!archivo.exists()) {
                System.err.println("❌ No se encontró el archivo PDF: " + rutaFactura);
                return false;
            }

            MimeBodyPart adjunto = new MimeBodyPart();
            DataSource source = new FileDataSource(archivo);
            adjunto.setDataHandler(new DataHandler(source));
            adjunto.setFileName(archivo.getName());

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            msg.setContent(multipart);

            // 📩 Enviar
            Transport.send(msg);
            System.out.println("✅ Correo con factura enviado a " + destinatario);
            return true;

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo con factura: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
