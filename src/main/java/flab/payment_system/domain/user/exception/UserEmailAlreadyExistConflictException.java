package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.ConflictException;

public class UserEmailAlreadyExistConflictException extends ConflictException {

	public UserEmailAlreadyExistConflictException() {
		super();
		this.message = "already_exist_user_email";
	}
}
