package flab.payment_system.domain.session.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Token {
	Access_Token("access_token", 7200);

	private final String tokenType;
	private final int maxExpireSecond;
}
