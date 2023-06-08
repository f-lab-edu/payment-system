package flab.payment_system.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.payment_system.domain.user.domain.QUserVerification;
import flab.payment_system.domain.user.dto.UserConfirmVerificationNumberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class UserVerificationCustomRepositoryImpl implements
	UserVerificationCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

	private final QUserVerification userVerification = QUserVerification.userVerification;

	@Transactional
	@Modifying
	@Override
	public long updateIsVerifiedByVerificationId(
		UserConfirmVerificationNumberDto userConfirmVerificationNumberDto) {
		return jpaQueryFactory.update(userVerification)
			.set(userVerification.isVerified, true)
			.where(userVerification.verificationId.eq(
				userConfirmVerificationNumberDto.verificationId())).execute();
	}
}
