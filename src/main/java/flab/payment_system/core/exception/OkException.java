package flab.payment_system.core.exception;

import org.springframework.http.HttpStatus;

public class OkException extends BaseException{
	protected OkException() {
		this.status = String.valueOf(HttpStatus.OK);
		this.code = HttpStatus.OK.value();
	}
}
