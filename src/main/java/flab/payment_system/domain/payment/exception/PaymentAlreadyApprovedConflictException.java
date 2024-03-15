package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.ConflictException;

public class PaymentAlreadyApprovedConflictException extends ConflictException {
	public PaymentAlreadyApprovedConflictException() {
		super();
		this.message = "payment_already_approved";
	}
}
