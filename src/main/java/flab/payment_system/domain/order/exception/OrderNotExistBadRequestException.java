package flab.payment_system.domain.order.exception;

import flab.payment_system.common.exception.BadRequestException;

public class OrderNotExistBadRequestException extends BadRequestException {

	public OrderNotExistBadRequestException() {
		super();
		this.message = "order_not_exist";
	}
}
