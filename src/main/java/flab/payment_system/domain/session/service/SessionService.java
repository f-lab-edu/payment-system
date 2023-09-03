package flab.payment_system.domain.session.service;

import flab.payment_system.domain.user.exception.UserNotSignInedConflictException;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class SessionService {


	public Optional<Long> getUserId(HttpSession session) {
		return Optional.ofNullable((Long) session.getAttribute("userId"));
	}

	public void setUserId(HttpSession session, Long userId) {
		session.setAttribute("userId", userId);
	}

	public void invalidate(HttpSession session) {
		session.invalidate();
	}
}
