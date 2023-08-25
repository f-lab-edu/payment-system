package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.ConflictException;

public class UserNotSignInedConflictException extends ConflictException {

	public UserNotSignInedConflictException() {
		super();
		this.message = "user_not_sign_in";
	}
}
