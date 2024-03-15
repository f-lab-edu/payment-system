package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.BadRequestException;

public class PaymentFailBadRequestException extends BadRequestException {
	public PaymentFailBadRequestException() {
		super();
		this.message = "payment_fail";
	}

}
