package flab.payment_system.domain.user.exception;

import flab.payment_system.common.exception.UnauthorizedException;


public class UserUnauthorizedException extends UnauthorizedException {

	public UserUnauthorizedException() {
		super();
		this.message = "unauthorized";
	}
}
