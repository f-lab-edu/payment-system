package flab.payment_system.domain.user.repository;

import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;

public interface UserVerificationCustomRepository {

	long updateIsVerifiedByVerificationId(
		UserConfirmVerificationNumberDto userConfirmVerificationNumberDto);
}
