package flab.payment_system.domain.user.service;

import flab.payment_system.domain.mail.service.MailService;
import flab.payment_system.domain.user.domain.User;
import flab.payment_system.domain.user.domain.UserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.exception.UserSignUpBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationEMailBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationIdBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationNumberBadRequestException;
import flab.payment_system.domain.user.exception.UserVerificationUnauthorizedException;
import flab.payment_system.domain.user.repository.UserRepository;
import flab.payment_system.domain.user.repository.UserVerificationRepository;
import java.util.Optional;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final MailService mailService;

	private final UserRepository userRepository;
	private final UserVerificationRepository userVerificationRepository;
	private final PasswordEncoder passwordEncoder;
	private final Random random;

	// 회원가입
	public void signUpUser(UserSignUpDto userSignUpDto) {

		// 이메일 인증된 유저인지 확인
		boolean isAuthorized = confirmUserIsAuthorized(userSignUpDto);
		if (!isAuthorized) {
			throw new UserVerificationUnauthorizedException();
		}

		// 비밀번호와 비밀번호 확인이 일치하는지 확인
		boolean isMatched = comparePasswordAndConfirmPassword(userSignUpDto);
		if (!isMatched) {
			throw new UserSignUpBadRequestException();
		}

		userRepository.save(User.builder()
			.eMail(userSignUpDto.eMail())
			.password(passwordEncoder.encode(userSignUpDto.password())).build());

	}

	// 회원가입을 위해 비밀번호와 비밀번호 확인이 일치하는지 확인
	private boolean comparePasswordAndConfirmPassword(UserSignUpDto userSignUpDto) {
		return passwordEncoder.matches(userSignUpDto.confirmPassword(),
			passwordEncoder.encode(userSignUpDto.password()));
	}

	// 회원가입을 위해 유저 인증 여부 확인 (유저가 인증메일을 받고, 인증까지 마쳤는지 확인)
	public boolean confirmUserIsAuthorized(UserSignUpDto userSignUpDto) {
		Optional<UserVerification> optionalUserVerification = userVerificationRepository.findById(
			userSignUpDto.verificationId());

		UserVerification userVerification = optionalUserVerification.orElseThrow(
			UserVerificationIdBadRequestException::new);

		if (!userVerification.isVerified()) {
			throw new UserVerificationUnauthorizedException();
		}

		return true;
	}

	// 회원가입 전 유저 인증을 위한 인증메일 발송
	public UserVerificationDto verifyUserEmail(UserDto userDto) {

		int verificationNumber = sendVerificationNumberToUserEmail(userDto);

		UserVerification userVerification = userVerificationRepository.save(
			UserVerification.builder()
				.eMail(userDto.eMail())
				.verificationNumber(verificationNumber).build());

		return new UserVerificationDto(
			userVerification.getVerificationId(), verificationNumber,
			userVerification.getEMail(), userVerification.isVerified());
	}

	// 회원가입 전 유저 인증을 위한 인증메일 발송
	public int sendVerificationNumberToUserEmail(UserDto userDto) {

		random.setSeed(System.currentTimeMillis());

		int verificationNumber = (random.nextInt(900000) + 100000) % 1000000;

		mailService.sendMail(userDto.eMail(),
			"[payment_system] 회원가입을 위한 인증번호 메일입니다.",
			mailService.setContextForSendValidationNumber(String.valueOf(verificationNumber)));

		return verificationNumber;
	}


	// 회원가입을 전 발송한 인증메일의 인증번호 확인
	public boolean confirmVerificationNumber(
		UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {
		Optional<UserVerification> optionalUserVerification = userVerificationRepository.findById(
			userConfirmVerificationNumberDto.verificationId());

		UserVerification userVerification = optionalUserVerification.orElseThrow(
			UserVerificationIdBadRequestException::new);

		if (!(userVerification.getEMail().equals(userConfirmVerificationNumberDto.eMail()))) {
			throw new UserVerificationEMailBadRequestException();
		}

		if (!(userVerification.getVerificationNumber()
			.equals(userConfirmVerificationNumberDto.verificationNumber()))) {
			throw new UserVerificationNumberBadRequestException();
		}

		userVerificationRepository.updateIsVerifiedByVerificationId(
			userConfirmVerificationNumberDto);

		return true;
	}

	public void signInUser(UserDto userDto) {
	}
}
