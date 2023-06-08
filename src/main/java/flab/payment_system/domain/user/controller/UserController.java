package flab.payment_system.domain.user.controller;

import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.service.UserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	// TODO bcrypt 암호화 후 비교
	// 회원가입
	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUpUser(@RequestBody UserSignUpDto userSignUpDto) {

		userService.signUpUser(userSignUpDto);

		return ResponseEntity.created(URI.create("/users/sign-up")).build();
	}

	// 회원가입을 위한 인증메일 발송
	@PostMapping("/e-mail")
	public ResponseEntity<UserVerificationDto> verifyUserEmail(@RequestBody UserDto userDto) {

		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userDto);

		return ResponseEntity.ok().body(userVerificationDto);
	}

	// 인증번호 확인
	@PostMapping("/e-mail/verification-number")
	public ResponseEntity<Object> confirmVerificationNumber(
		@RequestBody UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {

		userService.confirmVerificationNumber(userConfirmVerificationNumberDto);

		return ResponseEntity.noContent().build();
	}


	// 로그인
	@PostMapping("/sign-in")
	public ResponseEntity<Object> signInUser(@RequestBody UserDto userDto) {

		userService.signInUser(userDto);

		return ResponseEntity.noContent().build();
	}
}
