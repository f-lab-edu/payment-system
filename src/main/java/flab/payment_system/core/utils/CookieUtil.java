package flab.payment_system.core.utils;

import flab.payment_system.domain.session.enums.Token;
import flab.payment_system.domain.user.exception.UserNotSignInedConflictException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.http.ResponseCookie;

public class CookieUtil {

	private CookieUtil() {
	}

	public static ResponseCookie makeCookie(String key, String value, int maxAge) {
		return ResponseCookie.from(key, value)
			.maxAge(maxAge)
			.httpOnly(true)
			.path("/")
			.sameSite("Lax") // 분산 서버에서 None
			//.secure(true)
			.build();
	}

	public static ResponseCookie deleteCookie(String key) {
		return ResponseCookie.from(key, null)
			.maxAge(0)
			.httpOnly(true)
			.path("/")
			.sameSite("Lax") // 분산 서버에서 None
			//.secure(true)
			.build();
	}
}
