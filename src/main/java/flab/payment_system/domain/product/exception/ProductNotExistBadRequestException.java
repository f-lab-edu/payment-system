package flab.payment_system.domain.product.exception;

import flab.payment_system.core.exception.BadRequestException;

public class ProductNotExistBadRequestException extends BadRequestException {

	public ProductNotExistBadRequestException() {
		super();
		this.message = "product_not_exist";
	}
}
