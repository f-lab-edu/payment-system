package flab.payment_system.core.filter;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import flab.payment_system.core.utils.CookieUtil;
import flab.payment_system.domain.jwt.enums.Token;
import flab.payment_system.domain.jwt.service.JwtService;
import flab.payment_system.domain.user.exception.UserForbiddenException;
import flab.payment_system.domain.user.exception.UserUnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.ResponseCookie;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SignInCheckFilter extends OncePerRequestFilter {

	private final JwtService jwtService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		Cookie[] cookies = request.getCookies();
		Cookie accessTokenCookie = null;
		Cookie refreshTokenCookie = null;
		Map<Token, ResponseCookie> tokenCookies = null;
		String accessTokenValue = null;

		for (Cookie tempCookie : cookies) {
			if (accessTokenCookie != null && refreshTokenCookie != null) {
				break;
			}
			if (Objects.equals(tempCookie.getName(), Token.AccessToken.getTokenType())) {
				accessTokenCookie = tempCookie;
			}
			if (Objects.equals(tempCookie.getName(), Token.RefreshToken.getTokenType())) {
				refreshTokenCookie = tempCookie;
			}
		}
		if (accessTokenCookie == null && refreshTokenCookie == null) {
			throw new UserUnauthorizedException();
		}
		/*
		   RTR(Refresh Token Rotation) , TODO: 로그 작성 시 Refresh Token 을 사용해 재발급했는지 여부 저장
			Refresh Token 을 한번 사용하면 Access Token 발급 시 Refresh Token 도 함께 발급
		 */
		if (accessTokenCookie == null) {
			tokenCookies = jwtService.reissueTokenCookies(
				refreshTokenCookie);
			accessTokenValue = tokenCookies.get(Token.AccessToken).getValue();
		} else {
			accessTokenValue = accessTokenCookie.getValue();
		}

		String userId = jwtService.getUserIdFromJwt(
			accessTokenValue);

		HttpSession httpSession = request.getSession();
		String userSession = (String) httpSession.getAttribute(userId);

		if (userSession == null) {
			response.addHeader(SET_COOKIE, CookieUtil.deleteCookie(
				Token.AccessToken.getTokenType()).toString());
			response.addHeader(SET_COOKIE, CookieUtil.deleteCookie(
				Token.RefreshToken.getTokenType()).toString());

			throw new UserForbiddenException();
		}

		if (tokenCookies != null) {
			response.addHeader(SET_COOKIE, tokenCookies.get(Token.AccessToken).toString());
			response.addHeader(SET_COOKIE, tokenCookies.get(Token.RefreshToken).toString());
			httpSession.removeAttribute(userId);
			httpSession.setAttribute(userId, userId);
		}

		httpSession.removeAttribute(userId);
		httpSession.setAttribute(userId, userId);

		filterChain.doFilter(request, response);
	}
}
