package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.ForbiddenException;

public class UserForbiddenException extends ForbiddenException {

	public UserForbiddenException() {
		super();
		this.message = "forbidden";
	}
}
