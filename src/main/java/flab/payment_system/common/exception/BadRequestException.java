package flab.payment_system.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {

	protected BadRequestException() {
		this.status = String.valueOf(HttpStatus.BAD_REQUEST);
		this.code = HttpStatus.BAD_REQUEST.value();
	}
}
