package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.BadRequestException;

public class UserNotExistBadRequestException extends BadRequestException {
	public UserNotExistBadRequestException() {
		super();
		this.message = "user_not_exist";
	}
}
