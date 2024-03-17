package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundReceiveAccount {

	private String bankCode;
	private String accountNumber;
	private String holderName;
}
