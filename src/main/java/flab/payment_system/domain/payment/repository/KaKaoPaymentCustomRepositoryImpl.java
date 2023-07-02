package flab.payment_system.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class KaKaoPaymentCustomRepositoryImpl implements KakaoPaymentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

}
