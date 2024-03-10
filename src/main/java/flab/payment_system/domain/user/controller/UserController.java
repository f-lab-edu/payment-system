package flab.payment_system.domain.user.controller;

import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUpUser(@RequestBody @Valid UserSignUpDto userSignUpDto) {

		userService.signUpUser(userSignUpDto);

		return ResponseEntity.created(URI.create("/users/sign-up")).build();
	}

	@PostMapping("/email")
	public ResponseEntity<UserVerificationDto> verifyUserEmail(
		@RequestBody @Valid UserVerifyEmailDto userVerifyEmailDto) {

		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userVerifyEmailDto);

		return ResponseEntity.ok().body(userVerificationDto);
	}

	@PostMapping("/email/verification-number")
	public ResponseEntity<Object> confirmVerificationNumber(
		@RequestBody @Valid UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {

		userService.confirmVerificationNumber(userConfirmVerificationNumberDto);

		return ResponseEntity.noContent().build();
	}


	@PostMapping("/sign-in")
	public ResponseEntity<Object> signInUser(@RequestBody UserDto userDto, HttpSession session) {

		userService.signInUser(userDto, session);

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/sign-out")
	public ResponseEntity<Object> signOutUser(HttpSession session) {

		userService.signOutUser(session);

		return ResponseEntity.noContent().build();
	}
}
