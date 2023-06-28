package flab.payment_system.core.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {

	protected UnauthorizedException() {
		this.status = String.valueOf(HttpStatus.UNAUTHORIZED);
		this.code = HttpStatus.UNAUTHORIZED.value();
	}
}
