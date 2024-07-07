package flab.payment_system.common.exception;

import org.springframework.http.HttpStatus;

public class NotfoundException extends BaseException {

	protected NotfoundException() {
		this.status = String.valueOf(HttpStatus.NOT_FOUND);
		this.code = HttpStatus.NOT_FOUND.value();
	}
}
