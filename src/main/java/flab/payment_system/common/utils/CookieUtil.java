package flab.payment_system.common.utils;

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
