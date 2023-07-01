package flab.payment_system.domain.product.exception;

import flab.payment_system.core.exception.OkException;

public class ProductSoldOutException extends OkException {

	public ProductSoldOutException() {
		super();
		this.message = "product_sold_out";
	}
}
