package flab.payment_system.domain.user.dto;

public record UserSignUpDto(
	Long verificationId,
	String email,
	String password,
	String confirmPassword
) {

}
