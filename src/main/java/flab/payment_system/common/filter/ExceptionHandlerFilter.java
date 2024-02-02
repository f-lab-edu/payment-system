package flab.payment_system.common.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import flab.payment_system.common.exception.BaseException;
import flab.payment_system.common.exception.ExceptionMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

@Log4j2
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
		} catch (Exception exception) {
			handleException(exception, response);
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

		log.info(baseException.getStatus() + " : " + baseException.getMessage(), baseException);
	}

	private void handleException(Exception exception,
		HttpServletResponse response) throws IOException {
		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.INTERNAL_SERVER_ERROR + " : " + exception.getMessage())
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();

		byte[] responseToSend = restResponseBytes(exceptionMessage);

		response.setHeader("Content-Type", "application/json");
		response.setStatus(exceptionMessage.getCode());
		response.getOutputStream().write(responseToSend);

		log.error(HttpStatus.INTERNAL_SERVER_ERROR + " : " + exception.getMessage(), exception);
	}

	private byte[] restResponseBytes(ExceptionMessage exceptionMessage)
		throws IOException {
		String serialized = new ObjectMapper().writeValueAsString(exceptionMessage);
		return serialized.getBytes();
	}
}
