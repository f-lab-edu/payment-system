package flab.payment_system.core.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {

	protected ForbiddenException() {
		this.status = String.valueOf(HttpStatus.FORBIDDEN);
		this.code = HttpStatus.FORBIDDEN.value();
	}
}
