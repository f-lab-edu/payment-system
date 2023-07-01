package flab.payment_system.domain.session.service;

import flab.payment_system.core.utils.Sha256Util;
import flab.payment_system.domain.session.enums.Token;
import flab.payment_system.domain.user.exception.UserNotSignInedConflictException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SessionService {

	private final String secretKey;

	public SessionService(
		@Value("${secret-key}") String secretKey) {
		this.secretKey = secretKey;

	}


	public Long getByUserId(HttpSession session, Long userId) {
		return (Long) session.getAttribute(getCookieKey(userId));
	}

	public Long getByAccessToken(HttpSession session, String accessToken) {
		return (Long) session.getAttribute(accessToken);
	}

	public void setByUserId(HttpSession session, Long userId) {
		session.setMaxInactiveInterval(Token.Access_Token.getMaxExpireSecond());
		session.setAttribute(getCookieKey(userId), userId);
	}

	public void invalidate(HttpSession session) {
		session.invalidate();
	}

	public String getCookieKey(Long userId) {
		return Sha256Util.encrypt(secretKey + userId);
	}

	public Long getUserIdByRequest(HttpServletRequest request, HttpSession session) {
		Cookie[] cookies = Optional.ofNullable(request.getCookies())
			.orElseThrow(UserNotSignInedConflictException::new);

		Optional<Cookie> accessTokenCookie = Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(Token.Access_Token.getTokenType()))
			.findFirst();
		String accessToken = accessTokenCookie.orElseThrow(UserNotSignInedConflictException::new)
			.getValue();

		return Optional.ofNullable(
				getByAccessToken(session, accessToken))
			.orElseThrow(UserNotSignInedConflictException::new);
	}
}
