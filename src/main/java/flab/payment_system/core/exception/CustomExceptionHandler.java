package flab.payment_system.core.exception;

import flab.payment_system.domain.user.exception.UserEmailAlreadyExistConflictException;
import flab.payment_system.domain.user.exception.UserSignUpBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationEmailBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationIdBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationNumberBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationUnauthorizedException;
import jakarta.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// AOP : 예외 처리
@RequiredArgsConstructor
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	// 요청한 인증 정보가 없을 때 발생한 예외 처리
	@ExceptionHandler(UserVerificationIdBadRequestException.class)
	public ResponseEntity<ExceptionMessage> handleUserVerificationBadRequestException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.BAD_REQUEST + " : not_exist_verification_id")
			.code(HttpStatus.BAD_REQUEST.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	// 이메일 발송 후 유저가 입력한 인증번호가 올바르지 않을 때
	@ExceptionHandler(UserVerificationNumberBadRequestException.class)
	public ResponseEntity<ExceptionMessage> handleUserVerificationNumberBadRequestException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(
				HttpStatus.BAD_REQUEST + " : do_not_match_verification_number_and_user_input")
			.code(HttpStatus.BAD_REQUEST.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	// 이메일 발송 후 유저가 입력한 인증번호가 올바르지 않을 때
	@ExceptionHandler(UserVerificationEmailBadRequestException.class)
	public ResponseEntity<ExceptionMessage> handleUserEmailBadRequestException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(
				HttpStatus.BAD_REQUEST + " : do_not_match_email_find_by_verification_id")
			.code(HttpStatus.BAD_REQUEST.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}


	// 유저가 유효한 인증번호 입력 및 확인을 하지 않았을 때 발생한 예외 처리
	@ExceptionHandler(UserVerificationUnauthorizedException.class)
	public ResponseEntity<ExceptionMessage> handleUserVerificationUnauthorizedException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.UNAUTHORIZED + " : not_verified_email")
			.code(HttpStatus.UNAUTHORIZED.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.UNAUTHORIZED);
	}
	// 이미 존재하는 유저 이메일로 회원가입 요청했을 때 발생한 예외처리
	@ExceptionHandler(UserEmailAlreadyExistConflictException.class)
	public ResponseEntity<ExceptionMessage> handleUserEmailAlreadyExistConflictException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.CONFLICT + " : already_exist_user_email")
			.code(HttpStatus.CONFLICT.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.CONFLICT);
	}

	// 비밀번호와 비밀번호 확인이 일치하지 않았을 때 발생한 예외처리
	@ExceptionHandler(UserSignUpBadRequestException.class)
	public ResponseEntity<ExceptionMessage> handleUserSignUpBadRequestException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.BAD_REQUEST + " : do_not_match_password_and_confirm_password")
			.code(HttpStatus.BAD_REQUEST.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.BAD_REQUEST);
	}

	// 메일 발송 시 발생한 예외
	@ExceptionHandler(MessagingException.class)
	public ResponseEntity<ExceptionMessage> handleMessagingException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.INTERNAL_SERVER_ERROR + " : messaging_exception")
			.code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 지원하지 않는 인코딩 예외
	@ExceptionHandler(UnsupportedEncodingException.class)
	public ResponseEntity<ExceptionMessage> handleUnsupportedEncodingException() {

		ExceptionMessage exceptionMessage = ExceptionMessage.builder()
			.message(HttpStatus.UNSUPPORTED_MEDIA_TYPE + " : unsupportedEncodingException")
			.code(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()).build();

		return new ResponseEntity<>
			(exceptionMessage, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
	}
}
