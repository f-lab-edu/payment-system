package flab.payment_system.domain.jwt.enums;

import lombok.Getter;

@Getter
public enum Token {
	AccessToken("access_token", 1800),
	RefreshToken("refresh_token", 1209600);

	private final String tokenType;
	private final int maxExpireSecond;

	Token(String tokenType, int maxExpireSecond) {
		this.tokenType = tokenType;
		this.maxExpireSecond = maxExpireSecond;
	}
}
