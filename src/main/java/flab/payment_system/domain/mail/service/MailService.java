package flab.payment_system.domain.mail.service;

import flab.payment_system.domain.user.exception.UserVerifyUserEmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class MailService {

	private final JavaMailSender javaMailSender;
	private final SpringTemplateEngine templateEngine;

	private final String smtpId;

	@Autowired
	MailService(@Value("${smtp-id}") String smtpId, JavaMailSender javaMailSender,
		SpringTemplateEngine templateEngine) {
		this.javaMailSender = javaMailSender;
		this.templateEngine = templateEngine;
		this.smtpId = smtpId;

	}


	public void sendMail(String recipient, String subject,
		String context) {
		try {

			MimeMessage message = javaMailSender.createMimeMessage();

			message.addRecipients(MimeMessage.RecipientType.TO, recipient);
			message.setSubject(subject);
			message.setText(context,
				"utf-8",
				"html");
			message.setFrom(new InternetAddress(smtpId,
				"ps project"));

			javaMailSender.send(message);
		} catch (MessagingException | UnsupportedEncodingException exception) {
			throw new UserVerifyUserEmailException();
		}
	}

	public String setContextForSendValidationNumber(String verificationNumber) {
		Context context = new Context();
		context.setVariable("verificationNumber", verificationNumber);
		return templateEngine.process("mail", context);
	}

}
