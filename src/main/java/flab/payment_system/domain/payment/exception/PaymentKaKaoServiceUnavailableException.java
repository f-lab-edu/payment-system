package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.ServiceUnavailableException;

public class PaymentKaKaoServiceUnavailableException extends ServiceUnavailableException {

	public PaymentKaKaoServiceUnavailableException() {
		super();
		this.message = "kakao_service_unavailable";
	}
}
