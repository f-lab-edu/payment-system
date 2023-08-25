package flab.payment_system.common.exception;


import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log4j2
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ExceptionMessage> handleBaseException(BaseException baseException) {
		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(baseException.getStatus() + " : " + baseException.getMessage())
			.code(baseException.getCode()).build();

		log.info(baseException.getStatus() + " : " + baseException.getMessage(), baseException);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatusCode.valueOf(baseException.getCode()));
	}

	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionMessage> handleMessagingException(Exception exception) {
		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.INTERNAL_SERVER_ERROR + " : messaging_exception")
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();

		log.warn(HttpStatus.INTERNAL_SERVER_ERROR + " : messaging_exception", exception);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(UnsupportedEncodingException.class)
	public ResponseEntity<ExceptionMessage> handleUnsupportedEncodingException(
		Exception exception) {
		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.UNSUPPORTED_MEDIA_TYPE + " : unsupportedEncodingException")
			.code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()).build();

		log.warn(HttpStatus.UNSUPPORTED_MEDIA_TYPE + " : unsupportedEncodingException", exception);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ExceptionMessage> handleMethodArgumentNotValidException(
		MethodArgumentNotValidException exception) {
		List<String> errors = exception.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(x -> x.getDefaultMessage()).toList();

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.BAD_REQUEST + " : " + errors)
			.code(HttpStatus.BAD_REQUEST.value()).build();

		log.info(HttpStatus.BAD_REQUEST + " : " + errors, exception);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ExceptionMessage> handleHttpMessageNotReadableException(
		Exception exception) {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.BAD_REQUEST + " : " + exception.getMessage())
			.code(HttpStatus.BAD_REQUEST.value()).build();

		log.warn(HttpStatus.BAD_REQUEST + " : " + exception.getMessage(), exception);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ExceptionMessage> handleRuntimeException(
		Exception exception) {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.INTERNAL_SERVER_ERROR + " : " + exception.getMessage())
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();

		log.error(HttpStatus.INTERNAL_SERVER_ERROR + " : " + exception.getMessage(), exception);
		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
