package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.BadRequestException;

public class PaymentNotExistBadRequestException extends BadRequestException {
	public PaymentNotExistBadRequestException() {
		super();
		this.message = "payment_not_exist";
	}
}
