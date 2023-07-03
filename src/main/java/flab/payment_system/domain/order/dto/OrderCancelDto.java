package flab.payment_system.domain.order.dto;

public record OrderCancelDto(
	String tid,
	Integer cancelAmount,
	Integer cancelTaxFreeAmount,
	Integer orderId,
	Integer productId
) {

}
