package flab.payment_system.domain.order.dto;

import java.util.Optional;

public record OrderProductDto(
	String productName,
	long productId,
	int quantity,
	int totalAmount,
	int taxFreeAmount,
	int installMonth
) {

	public Optional<Integer> getInstallMonth() {
		return Optional.ofNullable(installMonth());
	}
};
