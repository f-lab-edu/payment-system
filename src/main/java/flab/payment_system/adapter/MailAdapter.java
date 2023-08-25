package flab.payment_system.adapter;

import flab.payment_system.domain.mail.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MailAdapter {

	private final MailService mailService;

	public void sendMail(String recipient, String subject, String context){
		mailService.sendMail(recipient, subject, context);
	}

	public String setContextForSendValidationNumber(String verificationNumber) {
		return mailService.setContextForSendValidationNumber(verificationNumber);
	}
}
