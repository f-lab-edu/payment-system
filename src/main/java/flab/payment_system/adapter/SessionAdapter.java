package flab.payment_system.adapter;

import flab.payment_system.session.service.SessionService;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SessionAdapter {

	private final SessionService sessionService;

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
