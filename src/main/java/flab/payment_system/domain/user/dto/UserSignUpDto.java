package flab.payment_system.domain.user.dto;

public record UserSignUpDto(
	Long verificationId,
	String eMail,
	String password,
	String confirmPassword
) {

}
