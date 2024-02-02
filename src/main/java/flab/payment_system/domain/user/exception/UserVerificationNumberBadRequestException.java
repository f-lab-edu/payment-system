package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.BadRequestException;

public class UserVerificationNumberBadRequestException extends BadRequestException {

	public UserVerificationNumberBadRequestException() {
		super();
		this.message = "do_not_match_verification_number_and_user_input";
	}
}
