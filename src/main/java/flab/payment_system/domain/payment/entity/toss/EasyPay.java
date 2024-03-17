package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EasyPay {

	private String provider;
	private Integer amount;
	private Integer discountAmount;
}
