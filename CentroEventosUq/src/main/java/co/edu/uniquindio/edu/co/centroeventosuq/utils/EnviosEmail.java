package co.edu.uniquindio.edu.co.centroeventosuq.utils;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Properties;

@AllArgsConstructor
@Data
public class EnviosEmail  {
    private String destinatario, asunto, mensaje,rutaImagen;


    private Session crearSesion() {
// Se definen las credenciales de la cuenta de correo
        final String username = "mariaproyectoprom2@gmail.com";
        final String password = "qrvrcftjdgezytrz";
// Se configuran las propiedades de la conexión
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
// Se crea un objeto de tipo Authenticator
        Authenticator authenticator = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        };

// Se crea la sesión
        return Session.getInstance(props, authenticator);
    }


    public void enviarNotificacion() {
        Session session =crearSesion();
        try {
// Se crea un objeto de tipo Message
            Message message = new MimeMessage(session);
// Se configura el remitente
            message.setFrom(new InternetAddress( "mariaproyectoprom2@gmail.com" ));
// Se configura el destinatario
            message.setRecipients( Message.RecipientType.TO, InternetAddress.parse(destinatario));
// Se configura el asunto del mensaje
            message.setSubject( asunto );
// Se configura el mensaje a enviar
            message.setText( mensaje );
// Se envía el mensaje
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void enviarNotificacionConImagen() {
        Session session = crearSesion();
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mariaproyectoprom2@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);

            // Parte del cuerpo del mensaje
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(mensaje);

            // Contenedor multipart para el cuerpo del mensaje y el archivo adjunto
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Parte del archivo adjunto
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(rutaImagen);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(rutaImagen);
            multipart.addBodyPart(messageBodyPart);

            // Configuración del contenido del mensaje
            message.setContent(multipart);

            // Enviar el mensaje
            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
