package Gestion_reserva;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;

public class CorreoEnviarReserva {

    public static void enviarCorreo(String destinatario, String asunto, String mensaje, String rutaPDF) {
        final String remitente = "sipacchuquiejj@gmail.com"; // tu correo
        final String password = "fzdp wmxq aixb puxo";       // tu contraseña de aplicación

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remitente, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Cuerpo + adjunto
            MimeBodyPart texto = new MimeBodyPart();
            texto.setText(mensaje);

            MimeBodyPart adjunto = new MimeBodyPart();
            adjunto.attachFile(new File(rutaPDF));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(texto);
            multipart.addBodyPart(adjunto);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("📧 Correo de reserva enviado a " + destinatario);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
