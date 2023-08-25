package flab.payment_system.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Log4j2
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, @NonNull HttpServletResponse response,
		@NonNull Object handler) {
		String requestURI = request.getRequestURI();

		log.info("REQUEST [{}][{}]", requestURI, handler);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,
		@NonNull Object handler, @Nullable ModelAndView modelAndView) {
		String queryString = request.getQueryString();

		log.info("RESPONSE [{}][{}][{}]", request.getMethod(),
			queryString == null ? request.getRequestURI()
				: request.getRequestURI() + queryString, response.getStatus());
	}
}
