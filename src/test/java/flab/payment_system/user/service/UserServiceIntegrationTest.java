package flab.payment_system.user.service;


import flab.payment_system.domain.user.domain.User;
import flab.payment_system.domain.user.domain.UserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.repository.UserRepository;
import flab.payment_system.domain.user.repository.UserVerificationRepository;
import flab.payment_system.domain.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserVerificationRepository userVerificationRepository;


	@DisplayName("이메일_인증번호_발급_성공")
	@Test
	public void verifyUserEmailSuccess() {
		// given
		String email = "payment@test.com";
		UserVerifyEmailDto userVerifyEmailDto = new UserVerifyEmailDto(email);

		// when
		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userVerifyEmailDto);

		// then
		Assertions.assertEquals(userVerificationDto.email(), email);

	}

	@DisplayName("이메일_인증_성공")
	@Test
	public void confirmVerificationNumberSuccess() {
		// given
		String email = "payment@test.com";
		UserVerifyEmailDto userVerifyEmailDto = new UserVerifyEmailDto(email);
		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userVerifyEmailDto);

		UserConfirmVerificationNumberDto userConfirmVerificationNumberDto = new UserConfirmVerificationNumberDto(
			userVerificationDto.verificationId(), userVerificationDto.email(),
			userVerificationDto.verificationNumber());

		// when
		boolean isVerified = userService.confirmVerificationNumber(
			userConfirmVerificationNumberDto);

		// then
		Assertions.assertEquals(isVerified, true);
	}

	@DisplayName("회원가입_성공")
	@Test
	public void signUpSuccess() {
		// given
		String email = "payment@test.com";
		String password = "12345";

		UserVerification userVerification = userVerificationRepository.save(
			UserVerification.builder()
				.email(email)
				.verificationNumber(123456).isVerified(false).build());

		UserSignUpDto userSignUpDto = new UserSignUpDto(userVerification.getVerificationId(),
			userVerification.getEmail(), password, password);

		userService.confirmVerificationNumber(new UserConfirmVerificationNumberDto(
			userVerification.getVerificationId(), userVerification.getEmail(),
			userVerification.getVerificationNumber()));

		// when
		userService.signUpUser(userSignUpDto);
		User user = userRepository.findByEmail(email).orElseThrow(RuntimeException::new);

		// then
		Assertions.assertEquals(email, user.getEmail());
	}
}
