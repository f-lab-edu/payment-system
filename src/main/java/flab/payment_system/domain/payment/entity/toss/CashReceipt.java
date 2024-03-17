package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashReceipt {

	private String type;
	private String receiptKey;
	private String issueNumber;
	private String receiptUrl;
	private Integer amount;
	private Integer taxFreeAmount;

}
