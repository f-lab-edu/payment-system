package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.ServiceUnavailableException;

public class PaymentTossServiceUnavailableException extends ServiceUnavailableException {

	public PaymentTossServiceUnavailableException() {
		super();
		this.message = "toss_service_unavailable";
	}
}

