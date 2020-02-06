package wolox.bootstrap.services;

import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;

import javax.mail.Message;

public class EmailService {

    private Mailer mailer;

    public EmailService(final String username, final String password) {
        mailer = new Mailer("smtp.gmail.com", 587, username, password,
            TransportStrategy.SMTP_TLS);
    }

    private Email generateEmail(final String senderName, final String senderEmail, final String recipientName,
                                final String recipientEmail, final String subject, final String body,
                                final String html) {
        Email email = new Email();
        email.setFromAddress(senderName, senderEmail);
        email.setSubject(subject);
        email.addRecipient(recipientName, recipientEmail, Message.RecipientType.TO);
        email.setText(body);
        email.setTextHTML(html);
        return email;
    }

    public void sendEmail(final String senderName, final String senderEmail, final String recipientName,
                          final String recipientEmail, final String subject, final String body, final String html) {
        Email email = generateEmail(senderName, senderEmail, recipientName, recipientEmail, subject,
            body, html);
        mailer.sendMail(email);
    }

}