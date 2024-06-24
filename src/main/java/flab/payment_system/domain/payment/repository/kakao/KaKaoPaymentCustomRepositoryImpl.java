package flab.payment_system.domain.payment.repository.kakao;

import com.querydsl.jpa.impl.JPAQueryFactory;

import flab.payment_system.domain.payment.entity.kakao.QKakaoPayment;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class KaKaoPaymentCustomRepositoryImpl implements KakaoPaymentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QKakaoPayment kakaoPayment = QKakaoPayment.kakaoPayment;

}
