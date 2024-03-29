package flab.payment_system.common.exception;

import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {

	protected ConflictException() {
		this.status = String.valueOf(HttpStatus.CONFLICT);
		this.code = HttpStatus.CONFLICT.value();
	}
}
