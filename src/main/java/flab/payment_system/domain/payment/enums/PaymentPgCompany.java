package flab.payment_system.domain.payment.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum PaymentPgCompany {
	KAKAO(0, "kakao"),
	TOSS(1, "toss");
	private final int value;
	private final String name;
}
