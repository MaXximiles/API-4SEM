package fatec.grupodois.endurance.service;

import com.sun.mail.smtp.SMTPTransport;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

import static fatec.grupodois.endurance.constant.EmailConstant.*;

@Service
public class EmailService {

    public void sendNewPasswordEmail(String firstName, String password, String email) throws MessagingException {
        Message message = createEmail(firstName, password, email);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendNewEventEmail(String firstName, String tema, String email) throws MessagingException {
        Message message = createEmailEvent(firstName, email, tema);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    public void sendNewEventConfirmedEmail(String firstName, String tema,
                                           String email, LocalDateTime inicio,
                                           LocalDateTime fim) throws MessagingException {
        Message message = createEmailEventConfirmed(firstName, email, tema, inicio, fim);
        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(GMAIL_SMTP_SERVER, USERNAME, PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }

    private Message createEmail(String firstName, String password, String email) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject(EMAIL_SUBJECT);

        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H3>Olá, " + firstName + ".</H3><br><p>Sua nova senha: "
                + password + "</p><br><br><img src=\"cid:image\">+<br><p>endurance Support Team</p>";

        messageBodyPart.setContent(htmlText, "text/html");

        multipart.addBodyPart(messageBodyPart);

        /*image*/
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("endurance_logo.png");

        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");

        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }

    private Message createEmailEvent(String firstName, String email, String tema) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("Novo Evento!");

        MimeMultipart multipart = new MimeMultipart("related");

        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H3>Olá, " + firstName + ".</H3><br><p>Um novo evento requer sua atenção: "
                + tema + "</p><br><br><img src=\"cid:image\">+<br><p>endurance Support Team</p>";

        messageBodyPart.setContent(htmlText, "text/html");

        multipart.addBodyPart(messageBodyPart);

        /*image*/
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource("endurance_logo.png");

        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");

        multipart.addBodyPart(messageBodyPart);

        message.setContent(multipart);

        /*message.setText("Olá, " + firstName + ".\n\nUm novo evento requer sua atenção: " + tema + "\n\n endurance Support Team" + "\n<img >");*/
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }

    private Message createEmailEventConfirmed(String firstName, String email,
                                              String tema, LocalDateTime inicio,
                                              LocalDateTime fim) throws MessagingException {
        Message message = new MimeMessage(getEmailSession());
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(CC_EMAIL, false));
        message.setSubject("Convite para novo evento");
        message.setText("Olá, "
                + firstName
                + ".\n\nUm novo evento, com o tema : "
                + tema
                + "foi confirmado! Aguardamos sua presença das "
                + inicio.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + " até "
                + fim.toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm"))
                + " no dia "
                + inicio.toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + "."
                + "\n\n Atenciosamente, endurance Support Team");
        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(SMTP_HOST, GMAIL_SMTP_SERVER);
        properties.put(SMTP_AUTH, true);
        properties.put(SMTP_PORT, DEFAULT_PORT);
        properties.put(SMTP_STARTTLS_ENABLE, true);
        properties.put(SMTP_STARTTLS_REQUIRED, true);

        return Session.getInstance(properties, null);
    }
}
