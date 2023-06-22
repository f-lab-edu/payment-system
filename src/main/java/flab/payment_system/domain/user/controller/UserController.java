package flab.payment_system.domain.user.controller;

import static org.springframework.http.HttpHeaders.SET_COOKIE;

import flab.payment_system.core.utils.CookieUtil;
import flab.payment_system.domain.jwt.enums.Token;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import flab.payment_system.domain.user.dto.UserDto;
import flab.payment_system.domain.user.dto.UserSignUpDto;
import flab.payment_system.domain.user.dto.UserVerificationDto;
import flab.payment_system.domain.user.dto.UserVerifyEmailDto;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping("/sign-up")
	public ResponseEntity<Object> signUpUser(@RequestBody @Valid UserSignUpDto userSignUpDto) {

		userService.signUpUser(userSignUpDto);

		return ResponseEntity.created(URI.create("/users/sign-up")).build();
	}

	@PostMapping("/e-mail")
	public ResponseEntity<UserVerificationDto> verifyUserEmail(
		@RequestBody @Valid UserVerifyEmailDto userVerifyEmailDto) {

		UserVerificationDto userVerificationDto = userService.verifyUserEmail(userVerifyEmailDto);

		return ResponseEntity.ok().body(userVerificationDto);
	}

	@PostMapping("/e-mail/verification-number")
	public ResponseEntity<Object> confirmVerificationNumber(
		@RequestBody @Valid UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {

		userService.confirmVerificationNumber(userConfirmVerificationNumberDto);

		return ResponseEntity.noContent().build();
	}


	// 로그인
	@PostMapping("/sign-in")
	public ResponseEntity<Object> signInUser(@RequestBody UserDto userDto,
		HttpSession httpSession, HttpServletResponse response) {

		Map<Token, ResponseCookie> tokenCookies = userService.signInUser(userDto, httpSession,
			response);

		response.addHeader(SET_COOKIE, tokenCookies.get(Token.AccessToken).toString());
		response.addHeader(SET_COOKIE, tokenCookies.get(Token.RefreshToken).toString());

		return ResponseEntity.noContent().build();
	}

	@PostMapping("/sign-out")
	public ResponseEntity<Object> signOutUser(@CookieValue("access_token") Cookie cookie,
		HttpSession httpSession,
		HttpServletResponse response) {
		userService.signOutUser(cookie, httpSession);

		response.addHeader(SET_COOKIE,
			CookieUtil.deleteCookie(Token.AccessToken.getTokenType()).toString());
		response.addHeader(SET_COOKIE,
			CookieUtil.deleteCookie(Token.RefreshToken.getTokenType()).toString());

		return ResponseEntity.noContent().build();
	}

	@GetMapping("/test")
	public String test() {
		return "success";
	}
}
