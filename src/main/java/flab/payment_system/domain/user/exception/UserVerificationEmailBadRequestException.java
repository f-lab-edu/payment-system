package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.BadRequestException;

public class UserVerificationEmailBadRequestException extends BadRequestException {
	public UserVerificationEmailBadRequestException() {
		super();
		this.message = "do_not_match_email_find_by_verification_id";
	}
}
