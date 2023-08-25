package flab.payment_system.adapter;

import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAdapter {

	private final UserService userService;

	public long getUserId(HttpSession session) {
		return userService.getUserId(session);
	}
}
