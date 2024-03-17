package flab.payment_system.domain.payment.entity.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transfer {

	private String bankCode;
	private String settlementStatus;
}
