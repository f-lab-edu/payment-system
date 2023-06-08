package flab.payment_system.domain.user.dto;


public record UserVerificationDto(
	Long verificationId,
	Integer verificationNumber,
	String eMail,
	boolean isVerified) {

}
