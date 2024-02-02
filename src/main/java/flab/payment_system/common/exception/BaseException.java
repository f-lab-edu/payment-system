package flab.payment_system.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	protected String status;

	protected String message;
	protected int code;

}
