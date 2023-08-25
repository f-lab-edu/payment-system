package flab.payment_system.common.exception;

import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends BaseException {

	protected ServiceUnavailableException() {
		this.status = String.valueOf(HttpStatus.SERVICE_UNAVAILABLE);
		this.code = HttpStatus.SERVICE_UNAVAILABLE.value();
	}
}
