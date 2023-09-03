package flab.payment_system.domain.payment.domain.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MobilePhone {

	private String customerMobilePhone;
	private String settlementStatus;
	private String receiptUrl;
}
