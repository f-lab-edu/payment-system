package flab.payment_system.domain.payment.exception;

import flab.payment_system.common.exception.ConflictException;

public class PaymentNotApprovedConflictException extends ConflictException {
	public PaymentNotApprovedConflictException() {
		super();
		this.message = "payment_not_approved";
	}
}
