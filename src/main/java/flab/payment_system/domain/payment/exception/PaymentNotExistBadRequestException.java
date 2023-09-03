package flab.payment_system.domain.payment.exception;

import flab.payment_system.core.exception.BadRequestException;

public class PaymentNotExistBadRequestException extends BadRequestException {
	public PaymentNotExistBadRequestException() {
		super();
		this.message = "payment_not_exist";
	}
}
