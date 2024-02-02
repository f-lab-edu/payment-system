package flab.payment_system.common.filter;

import flab.payment_system.session.service.SessionService;
import flab.payment_system.domain.user.exception.UserUnauthorizedException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
@RequiredArgsConstructor
public class SignInCheckFilter extends OncePerRequestFilter {

	private final SessionService sessionService;

	@Override
	protected void doFilterInternal(HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain) throws ServletException, IOException {
		Optional.ofNullable(request.getSession(false))
			.orElseThrow(UserUnauthorizedException::new);

		sessionService.getUserId(request.getSession(false))
			.orElseThrow(UserUnauthorizedException::new);

		log.info("check sign in {}", request.getRequestURI());
		filterChain.doFilter(request, response);
	}
}
