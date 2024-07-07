package flab.payment_system.domain.compensation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PGManageName {
	KAKAO("KAKAO", "not_found_corrections_kakao_", "inconsistent_corrections_kakao_", "not_found_kakao",
		"inconsistent_kakao"),
	TOSS("TOSS", "not_found_corrections_toss_", "inconsistent_corrections_toss_", "not_found_kakao",
		"inconsistent_toss");

	private final String pgCompanyName;
	private final String notFoundFileName;
	private final String inconsistentFileName;
	private final String batchContextNotFoundName;
	private final String batchContextInconsistentName;
}
