package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.BadRequestException;

public class UserVerificationIdBadRequestException extends BadRequestException {

	public UserVerificationIdBadRequestException() {
		super();
		this.message = "not_exist_verification_id";
	}
}
