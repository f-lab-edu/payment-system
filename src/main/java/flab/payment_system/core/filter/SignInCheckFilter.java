package flab.payment_system.core.filter;

import flab.payment_system.domain.session.enums.Token;
import flab.payment_system.domain.session.service.SessionService;
import flab.payment_system.domain.user.exception.UserUnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class SignInCheckFilter extends OncePerRequestFilter {

	private final SessionService sessionService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		HttpSession session = Optional.ofNullable(request.getSession(false))
			.orElseThrow(UserUnauthorizedException::new);

		Optional<Cookie> accessTokenCookie = Arrays.stream(request.getCookies())
			.filter(cookie -> cookie.getName().equals(Token.Access_Token.getTokenType()))
			.findFirst();

		if (accessTokenCookie.isEmpty()) {
			sessionService.invalidate(session);
			throw new UserUnauthorizedException();
		}

		Optional<Long> userSession = Optional.ofNullable(
			sessionService.getByAccessToken(session, accessTokenCookie.get().getValue()));

		if (userSession.isEmpty()) {
			sessionService.invalidate(session);
			throw new UserUnauthorizedException();
		}

		filterChain.doFilter(request, response);
	}
}
