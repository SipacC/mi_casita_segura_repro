package Gestion_qr;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class correoEnviarQr {

    private final String remitente = "sipacchuquiejj@gmail.com";   // ⚠️ tu correo
    private final String password = "fzdp wmxq aixb puxo";       // ⚠️ App Password (no tu clave normal)

    /**
     * Envía un correo con el QR adjunto
     * @param destinatario correo del usuario recién creado (per.getCorreo())
     * @param nombreUsuario nombre del usuario (per.getNombres())
     * @param rutaQR ruta absoluta al archivo PNG generado (CrearQr.generarQR)
     */
    public boolean enviarConQR(String destinatario, String nombreUsuario, String rutaQR) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, password);
            }
        });

        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remitente));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            msg.setSubject("Notificación de accesos creados");

            // Cuerpo del mensaje
            String cuerpo = "¡Hola!\n\n"
                    + "Se ha generado exitosamente tu código QR de acceso al residencial.\n"
                    + "A continuación, encontrarás los detalles de tu registro:\n\n"
                    + "Nombre del Usuario: " + nombreUsuario + "\n"
                    + "Validez del código QR: Permanente\n\n"
                    + "Instrucciones importantes:\n"
                    + "- Guarda este correo o el código QR adjunto.\n"
                    + "- Preséntalo al llegar al residencial para que el personal de seguridad lo escanee y valide tu acceso.\n";

            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(cuerpo, "utf-8");

            // Adjuntar QR
            MimeBodyPart adjunto = new MimeBodyPart();
            DataSource source = new FileDataSource(rutaQR);
            adjunto.setDataHandler(new DataHandler(source));
            adjunto.setFileName("qr_usuario.png");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            msg.setContent(multipart);

            Transport.send(msg);
            System.out.println("Correo enviado a " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}
