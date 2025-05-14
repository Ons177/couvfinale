package entities;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailService {

    public static void send(String to, String subject, String content, final String user, final String appPassword, boolean isHtml) {
        // Configuration des propriétés SMTP pour Gmail avec SSL
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, appPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            // Envoi du contenu HTML ou texte
            if (isHtml) {
                message.setContent(content, "text/html; charset=utf-8");
            } else {
                message.setText(content);
            }

            // Tâche d'envoi en arrière-plan
            Task<Void> emailTask = new Task<>() {
                @Override
                protected Void call() {
                    try {
                        Transport.send(message);
                        System.out.println("✅ E-mail envoyé avec succès à " + to);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                        showErrorAlert("Erreur d'envoi", "L'e-mail n'a pas pu être envoyé : " + e.getMessage());
                    }
                    return null;
                }
            };

            new Thread(emailTask).start();

        } catch (MessagingException e) {
            e.printStackTrace();
            showErrorAlert("Erreur de création", "Impossible de créer l’e-mail : " + e.getMessage());
        }
    }

    private static void showErrorAlert(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}


