package flab.payment_system.domain.payment.dto;

import java.io.Serial;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentCompensationDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	Long orderId;
	String paymentKey;
	Integer totalAmount;
	Integer paymentState;
	Integer taxFreeAmount;
	Integer installMonth;
}
