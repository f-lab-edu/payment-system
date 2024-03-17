package flab.payment_system.domain.order.dto;

import jakarta.validation.constraints.Min;

public record OrderProductDto
	(
		@Min(value = 1, message = "invalid_product_id")
		Long productId,
		@Min(value = 1, message = "invalid_quantity")
		Integer quantity) {
}
