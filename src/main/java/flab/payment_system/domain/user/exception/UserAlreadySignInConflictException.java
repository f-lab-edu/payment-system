package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.ConflictException;


public class UserAlreadySignInConflictException extends ConflictException {
	public UserAlreadySignInConflictException() {
		super();
		this.message = "user_already_sign_in";
	}
}
