package flab.payment_system.domain.user.service;

import flab.payment_system.domain.mail.service.MailService;
import flab.payment_system.domain.user.domain.User;
import flab.payment_system.domain.user.domain.UserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.exception.UserEmailAlreadyExistConflictException;
import flab.payment_system.domain.user.exception.UserVerificationEmailBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationIdBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationNumberBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationUnauthorizedException;
import flab.payment_system.domain.user.repository.UserRepository;
import flab.payment_system.domain.user.repository.UserVerificationRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final MailService mailService;

	private final UserRepository userRepository;
	private final UserVerificationRepository userVerificationRepository;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public void signUpUser(UserSignUpDto userSignUpDto) {
		Optional<User> optionalUser = userRepository.findByEmail(userSignUpDto.email());
		if (optionalUser.isPresent()) {
			throw new UserEmailAlreadyExistConflictException();
		}

		confirmUserIsAuthorized(userSignUpDto);


		userRepository.save(User.builder()
			.email(userSignUpDto.email())
			.password(passwordEncoder.encode(userSignUpDto.password())).build());

	}

	}

	public void confirmUserIsAuthorized(UserSignUpDto userSignUpDto) {
		Optional<UserVerification> optionalUserVerification = userVerificationRepository.findById(
			userSignUpDto.verificationId());

		UserVerification userVerification = optionalUserVerification.orElseThrow(
			UserVerificationIdBadRequestException::new);

		if (!userVerification.getEmail().equals(userSignUpDto.email())) {
			throw new UserVerificationEmailBadRequestException();
		}

		if (!userVerification.isVerified()) {
			throw new UserVerificationUnauthorizedException();
		}
	}

	public UserVerificationDto verifyUserEmail(UserVerifyEmailDto userVerifyEmailDto) {

		int verificationNumber = sendVerificationNumberToUserEmail(userVerifyEmailDto);

		UserVerification userVerification = userVerificationRepository.save(
			UserVerification.builder()
				.email(userVerifyEmailDto.email())
				.verificationNumber(verificationNumber).build());

		return new UserVerificationDto(
			userVerification.getVerificationId(), verificationNumber,
			userVerification.getEmail(), userVerification.isVerified());
	}

	public int sendVerificationNumberToUserEmail(UserVerifyEmailDto userVerifyEmailDto) {
		ThreadLocalRandom random = ThreadLocalRandom.current();

		int verificationNumber = (random.nextInt(900000) + 100000) % 1000000;

		mailService.sendMail(userVerifyEmailDto.email(),
			"[payment_system] 회원가입을 위한 인증번호 메일입니다.",
			mailService.setContextForSendValidationNumber(String.valueOf(verificationNumber)));

		return verificationNumber;
	}


	public boolean confirmVerificationNumber(
		UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {
		Optional<UserVerification> optionalUserVerification = userVerificationRepository.findById(
			userConfirmVerificationNumberDto.verificationId());

		UserVerification userVerification = optionalUserVerification.orElseThrow(
			UserVerificationIdBadRequestException::new);

		if (!(userVerification.getEmail().equals(userConfirmVerificationNumberDto.email()))) {
			throw new UserVerificationEmailBadRequestException();
		}

		if (!(userVerification.getVerificationNumber()
			.equals(userConfirmVerificationNumberDto.verificationNumber()))) {
			throw new UserVerificationNumberBadRequestException();
		}

		userVerificationRepository.updateIsVerifiedByVerificationId(
			userConfirmVerificationNumberDto);

		return true;
	}

	public Map<Token, ResponseCookie> signInUser(UserDto userDto, HttpSession httpSession,
		HttpServletResponse response) {
		Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

		User user = optionalUser.orElseThrow(UserEmailNotExistBadRequestException::new);

		boolean isPasswordConfirmed = comparePasswordAndConfirmPassword(user.getPassword(),
			userDto.password());

		if (!isPasswordConfirmed) {
			throw new UserPasswordFailBadRequestException();
		}

		String userId = String.valueOf(user.getUserId());

		String userSession = (String) httpSession.getAttribute(userId);

		if (userSession != null) {
			httpSession.removeAttribute(userId);
			response.addHeader(SET_COOKIE,
				CookieUtil.deleteCookie(Token.AccessToken.getTokenType()).toString());
			response.addHeader(SET_COOKIE,
				CookieUtil.deleteCookie(Token.RefreshToken.getTokenType()).toString());

			throw new UserAlreadySignInConflictException();
		}

		httpSession.setAttribute(userId, userId);

		return jwtService.issueTokenCookies(userId);
	}

	public void signOutUser(Cookie cookie, HttpSession httpSession) {
		String accessToken = cookie.getValue();
		String userId = jwtService.getUserIdFromJwt(accessToken);

		httpSession.removeAttribute(userId);
	}
}
