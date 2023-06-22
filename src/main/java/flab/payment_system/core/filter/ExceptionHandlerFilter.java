package flab.payment_system.core.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import flab.payment_system.core.exception.BaseException;
import flab.payment_system.core.exception.ExceptionMessage;
import flab.payment_system.domain.user.exception.UserForbiddenException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;


public class ExceptionHandlerFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		@NonNull HttpServletRequest request,
		@NonNull HttpServletResponse response,
		@NonNull FilterChain filterChain
	) throws IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (BaseException baseException) {
			handleBaseException(baseException, response);
		} catch (IOException | ServletException exception) {
			handleBaseException(new UserForbiddenException(), response);
		} catch (Exception exception) {
			handleException(new RuntimeException(), response);
		}
	}

	private void handleBaseException(BaseException baseException,
		HttpServletResponse response) throws IOException {
		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(baseException.getStatus() + " : " + baseException.getMessage())
			.code(baseException.getCode()).build();

		byte[] responseToSend = restResponseBytes(exceptionMessage);

		response.setHeader("Content-Type", "application/json");
		response.setStatus(exceptionMessage.getCode());
		response.getOutputStream().write(responseToSend);
	}

	private void handleException(Exception exception,
		HttpServletResponse response) throws IOException {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR))
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();

		byte[] responseToSend = restResponseBytes(exceptionMessage);

		response.setHeader("Content-Type", "application/json");
		response.setStatus(exceptionMessage.getCode());
		response.getOutputStream().write(responseToSend);
	}

	private byte[] restResponseBytes(ExceptionMessage exceptionMessage)
		throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(exceptionMessage);
		return serialized.getBytes();
	}
}
