package flab.payment_system.domain.user.service;

import flab.payment_system.adapter.UserAdapter;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.entity.UserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.exception.*;
import flab.payment_system.domain.user.repository.UserRepository;
import flab.payment_system.domain.user.repository.UserVerificationRepository;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserAdapter userAdapter;

	private final UserRepository userRepository;
	private final UserVerificationRepository userVerificationRepository;
	private final PasswordEncoder passwordEncoder;

	public void signUpUser(UserSignUpDto userSignUpDto) {
		Optional<User> optionalUser = userRepository.findByEmail(userSignUpDto.email());
		if (optionalUser.isPresent()) {
			throw new UserEmailAlreadyExistConflictException();
		}

		confirmUserIsAuthorized(userSignUpDto);

		boolean isPasswordConfirmed = comparePasswordAndConfirmPassword(
			passwordEncoder.encode(userSignUpDto.password()), userSignUpDto.confirmPassword());

		if (!isPasswordConfirmed) {
			throw new UserSignUpBadRequestException();
		}

		userRepository.save(User.builder()
			.email(userSignUpDto.email())
			.password(passwordEncoder.encode(userSignUpDto.password())).build());


	}

	private boolean comparePasswordAndConfirmPassword(String hashedPassword,
													  String comparedPassword) {
		return passwordEncoder.matches(comparedPassword, hashedPassword);
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
		Optional<User> optionalUser = userRepository.findByEmail(userVerifyEmailDto.email());

		if (optionalUser.isPresent()) {
			throw new UserEmailAlreadyExistConflictException();
		}

		int verificationNumber = sendVerificationNumberToUserEmail(userVerifyEmailDto);

		UserVerification userVerification = userVerificationRepository.save(
			UserVerification.builder()
				.email(userVerifyEmailDto.email())
				.verificationNumber(verificationNumber).isVerified(false).build());

		return new UserVerificationDto(
			userVerification.getVerificationId(), verificationNumber,
			userVerification.getEmail(), userVerification.isVerified());
	}

	public int sendVerificationNumberToUserEmail(UserVerifyEmailDto userVerifyEmailDto) {
		ThreadLocalRandom random = ThreadLocalRandom.current();

		int verificationNumber = (random.nextInt(900000) + 100000) % 1000000;

		userAdapter.sendMail(userVerifyEmailDto.email(),
			"[payment_system] 회원가입을 위한 인증번호 메일입니다.",
			userAdapter.setContextForSendValidationNumberForSendMail(String.valueOf(verificationNumber)));

		return verificationNumber;
	}


	@Transactional
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

		userVerificationRepository.save(
			UserVerification.builder()
				.verificationId(userConfirmVerificationNumberDto.verificationId())
				.email(userConfirmVerificationNumberDto.email())
				.verificationNumber(userConfirmVerificationNumberDto.verificationNumber())
				.isVerified(true).build());

		return true;
	}

	@Transactional(readOnly = true)
	public void signInUser(UserDto userDto, HttpSession session) {
		if (userAdapter.getUserId(session).isPresent()) {
			throw new UserAlreadySignInConflictException();
		}

		Optional<User> optionalUser = userRepository.findByEmail(userDto.email());

		User user = optionalUser.orElseThrow(UserEmailNotExistBadRequestException::new);

		boolean isPasswordConfirmed = comparePasswordAndConfirmPassword(user.getPassword(),
			userDto.password());

		if (!isPasswordConfirmed) {
			throw new UserPasswordFailBadRequestException();
		}

		userAdapter.setUserId(session, user.getUserId());
	}

	public void signOutUser(HttpSession session) {
		getUserId(session);
		userAdapter.invalidate(session);
	}

	public Long getUserId(HttpSession session) {
		return userAdapter.getUserId(session).orElseThrow(UserNotSignInedConflictException::new);
	}

	@Transactional(readOnly = true)
	public User getUserByUserId(Long userId) {
		return userRepository.findById(userId).orElseThrow(UserNotExistBadRequestException::new);
	}
}
