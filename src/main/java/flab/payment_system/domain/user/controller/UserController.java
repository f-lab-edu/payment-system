package flab.payment_system.domain.user.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import flab.payment_system.core.utils.CookieUtil;
import flab.payment_system.domain.session.enums.Token;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	public ResponseEntity<Object> signInUser(@RequestBody UserDto userDto,
		HttpSession session) {

		String cookieValue = userService.signInUser(userDto, session);

		return ResponseEntity.noContent()
			.header(SET_COOKIE, CookieUtil.makeCookie(Token.Access_Token.getTokenType(),
				cookieValue, Token.Access_Token.getMaxExpireSecond()).toString()).build();
	}

	@PostMapping("/sign-out")
	public ResponseEntity<Object> signOutUser(HttpServletRequest request, HttpSession session) {

		userService.signOutUser(request, session);

		return ResponseEntity.noContent()
			.header(SET_COOKIE, CookieUtil.deleteCookie(Token.Access_Token.getTokenType()).toString()).build();
	}

	@GetMapping("/test")
	public String test() {
		return "success";
	}
}
