package flab.payment_system.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignUpDto(
	Long verificationId,
	@Email(regexp = "^([\\w\\.\\_\\-])*[a-zA-Z0-9]+([\\w\\.\\_\\-])*([a-zA-Z0-9])+([\\w\\.\\_\\-])+@([a-zA-Z0-9]+\\.)+[a-zA-Z0-9]{2,3}$", message = "invalid_email")
	@Size(min = 2, max = 350, message = "invalid_email")
	String email,
	@NotBlank(message = "invalid_password")
	@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,16}$", message = "invalid_password")
	String password,
	String confirmPassword
) {

}
