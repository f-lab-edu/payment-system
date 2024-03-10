package flab.payment_system.adapter;

import flab.payment_system.domain.mail.service.MailService;
import flab.payment_system.session.service.SessionService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class UserAdapterImpl implements UserAdapter {

	private final SessionService sessionService;
	private final MailService mailService;

	public void sendMail(String recipient, String subject, String context) {
		mailService.sendMail(recipient, subject, context);
	}

	public String setContextForSendValidationNumberForSendMail(String verificationNumber) {
		return mailService.setContextForSendValidationNumber(verificationNumber);
	}

	public Optional<Long> getUserId(HttpSession session) {
		return sessionService.getUserId(session);
	}

	public void setUserId(HttpSession session, Long userId) {
		sessionService.setUserId(session, userId);
	}

	public void invalidate(HttpSession session) {
		sessionService.invalidate(session);
	}
}
