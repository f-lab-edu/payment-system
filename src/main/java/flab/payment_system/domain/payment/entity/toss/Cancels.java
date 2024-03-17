package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Cancels {

	private Integer cancelAmount;
	private String cancelReason;
	private Integer taxFreeAmount;
	private Integer taxExemptionAmount;
	private Integer refundableAmount;
	private Integer easyPayDiscountAmount;
	private String canceledAt;
	private String transactionKey;
	private String receiptKey;
}
