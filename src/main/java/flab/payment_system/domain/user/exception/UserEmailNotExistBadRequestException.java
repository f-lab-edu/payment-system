package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.BadRequestException;

public class UserEmailNotExistBadRequestException extends BadRequestException {

	public UserEmailNotExistBadRequestException() {
		super();
		this.message = "user_email_auth_fail";
	}
}
