package flab.payment_system.domain.product.dto;

import jakarta.validation.constraints.Min;

public record ProductDto(
	@Min(value = 1, message = "invalid_product_id")
	Long productId,
	String name,
	@Min(value = 1, message = "invalid_price")
	Integer price,
	@Min(value = 1, message = "invalid_stock")
	Integer stock) {

}
