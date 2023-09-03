package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.BadRequestException;


public class UserSignUpBadRequestException extends BadRequestException {

	public UserSignUpBadRequestException() {
		super();
		this.message = "do_not_match_password_and_confirm_password";
	}
}
