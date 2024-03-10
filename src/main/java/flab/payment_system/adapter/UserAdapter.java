package flab.payment_system.adapter;

import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public interface UserAdapter {
	void sendMail(String recipient, String subject, String context);

	String setContextForSendValidationNumberForSendMail(String verificationNumber);

	Optional<Long> getUserId(HttpSession session);

	void setUserId(HttpSession session, Long userId);

	void invalidate(HttpSession session);
}
