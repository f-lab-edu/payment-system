package flab.payment_system.domain.payment.dto;

import jakarta.validation.constraints.Min;

import java.util.Optional;

public record PaymentCreateDto(
	Long orderId,
	String productName,
	@Min(value = 1, message = "invalid_product_id")
	Long productId,
	@Min(value = 1, message = "invalid_quantity")
	Integer quantity,
	@Min(value = 1, message = "invalid_total_amount")
	Integer totalAmount,
	@Min(value = 1, message = "invalid_tax_free_amount")
	Integer taxFreeAmount,
	Integer installMonth
) {

	public Optional<Integer> getInstallMonth() {
		return Optional.ofNullable(installMonth());
	}
};
