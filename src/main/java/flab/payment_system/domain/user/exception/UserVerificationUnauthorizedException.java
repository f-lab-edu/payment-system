package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.UnauthorizedException;

public class UserVerificationUnauthorizedException extends UnauthorizedException {

	public UserVerificationUnauthorizedException() {
		super();
		this.message = "not_verified_email";
	}
}
