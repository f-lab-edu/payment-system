package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CashReceipts {

	private String receiptKey;
	private String orderId;
	private String orderName;
	private String type;
	private String issueNumber;
	private String receiptUrl;
	private String businessNumber;
	private String transactionType;
	private Integer amount;
	private Integer taxFreeAmount;
	private String issueStatus;
	private Failure failure;
	private String customerIdentityNumber;
	private String requestedAt;
}
