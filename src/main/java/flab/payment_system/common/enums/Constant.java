package flab.payment_system.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Constant {
	API_AND_VERSION("/api/v1");


	private final String value;
}
