package flab.payment_system.domain.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStateConstant {
	ONGOING(0),

	APPROVED(1),
	FAIL(2),
	CANCEL(3);
	private final Integer value;
}
