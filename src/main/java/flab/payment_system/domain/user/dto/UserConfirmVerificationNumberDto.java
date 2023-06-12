package flab.payment_system.domain.user.dto;

public record UserConfirmVerificationNumberDto(
	Long verificationId,
	String email,
	Integer verificationNumber
) {

}
