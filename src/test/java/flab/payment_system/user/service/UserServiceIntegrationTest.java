package flab.payment_system.user.service;


import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.entity.UserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.repository.UserRepository;
import flab.payment_system.domain.user.repository.UserVerificationRepository;
import flab.payment_system.domain.user.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class UserServiceIntegrationTest {

	private final UserService userService;

	private final UserRepository userRepository;

	private final UserVerificationRepository userVerificationRepository;

	private final DatabaseCleanUp databaseCleanUp;

	@Autowired
	UserServiceIntegrationTest
		(UserService userService, UserRepository userRepository,
		 UserVerificationRepository userVerificationRepository, DatabaseCleanUp databaseCleanUp) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.userVerificationRepository = userVerificationRepository;
		this.databaseCleanUp = databaseCleanUp;

	}

	@BeforeEach
	void setUp() {
		databaseCleanUp.truncateAllEntity();
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllEntity();
	}


	@DisplayName("이메일_인증번호_발급_성공")
	@Test
	public void verifyUserEmailSuccess() {
		// given
		String email = "payment@test.com";
		UserVerifyEmailDto userVerifyEmailDto = new UserVerifyEmailDto(email);

		// when
		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userVerifyEmailDto);

		// then
		assertEquals(userVerificationDto.email(), email);

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
		assertTrue(isVerified);
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
		User user = userRepository.findByEmail(email).orElse(null);

		// then
		assertEquals(email, user.getEmail());
		assertNotNull(user);
	}
}
