package flab.payment_system.domain.order.dto;

import jakarta.validation.constraints.Min;
import java.util.Optional;

public record OrderProductDto(
	String productName,
	@Min(value = 1, message = "invalid_product_id")
	long productId,
	@Min(value = 1, message = "invalid_quantity")
	int quantity,
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
