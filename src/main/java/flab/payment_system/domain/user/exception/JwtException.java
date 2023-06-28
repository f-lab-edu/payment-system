package flab.payment_system.domain.user.exception;

import flab.payment_system.core.exception.BadRequestException;

public class JwtException extends BadRequestException {
	public JwtException() {
		super();
		this.message = "jwt_exception";
	}
}
