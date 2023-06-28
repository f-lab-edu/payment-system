package flab.payment_system.domain.user.dto;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserConfirmVerificationNumberDto(
	Long verificationId,
	@Email(regexp = "^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}$", message = "invalid_email")
	@Size(min = 2, max = 350, message = "invalid_email")
	String email,
	@Nonnull
	Integer verificationNumber
) {

}
