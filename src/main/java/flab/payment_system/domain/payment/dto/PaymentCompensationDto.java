package flab.payment_system.domain.payment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCompensationDto {
	Long orderId;
	String paymentKey;
	Integer totalAmount;
	Integer paymentState;
	Integer taxFreeAmount;
	Integer installMonth;
}
