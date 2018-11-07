package wolox.bootstrap.services;

import javax.mail.Message;
import org.simplejavamail.email.Email;
import org.simplejavamail.mailer.Mailer;
import org.simplejavamail.mailer.config.TransportStrategy;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class EmailService {

	private Mailer mailer;

	public EmailService(String username, String password) {
		mailer = new Mailer("smtp.gmail.com", 587, username, password,
			TransportStrategy.SMTP_TLS);
	}

	private Email generateEmail(String senderName, String senderEmail, String recipientName,
		String recipientEmail, String subject, String body, String html) {
		Email email = new Email();
		email.setFromAddress(senderName, senderEmail);
		email.setSubject(subject);
		email.addRecipient(recipientName, recipientEmail, Message.RecipientType.TO);
		email.setText(body);
		email.setTextHTML(html);
		return email;
	}

	public void sendEmail(String senderName, String senderEmail, String recipientName,
		String recipientEmail, String subject, String body, String html) {
		Email email = generateEmail(senderName, senderEmail, recipientName, recipientEmail, subject,
			body, html);
		mailer.sendMail(email);
	}

}