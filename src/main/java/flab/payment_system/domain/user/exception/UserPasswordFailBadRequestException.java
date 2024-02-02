package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.BadRequestException;

public class UserPasswordFailBadRequestException extends BadRequestException {

	public UserPasswordFailBadRequestException() {
		super();
		this.message = "user_password_auth_fail";
	}
}
