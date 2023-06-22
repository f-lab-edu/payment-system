package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.BadRequestException;


public class UserVerifyUserEmailException extends BadRequestException {
	public UserVerifyUserEmailException() {
		super();
		this.message = "send_mail_fail";
	}
}
