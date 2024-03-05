package flab.payment_system.domain.order.dto;

import jakarta.validation.constraints.Min;

public record OrderCancelDto(
	String paymentKey,
	@Min(value = 1, message = "invalid_cancel_amount")
	Integer cancelAmount,
	@Min(value = 1, message = "invalid_cancel_tax_free_amount")
	Integer cancelTaxFreeAmount,
	@Min(value = 1, message = "invalid_order_id")
	Long orderId,
	@Min(value = 1, message = "invalid_product_id")
	Long productId,
	@Min(value = 1, message = "invalid_quantity")
	Integer quantity
) {

}
