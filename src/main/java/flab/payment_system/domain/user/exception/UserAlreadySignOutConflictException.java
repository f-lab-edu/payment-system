package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.ConflictException;

public class UserAlreadySignOutConflictException extends ConflictException {

	public UserAlreadySignOutConflictException() {
		super();
		this.message = "user_already_sign_out";
	}
}
