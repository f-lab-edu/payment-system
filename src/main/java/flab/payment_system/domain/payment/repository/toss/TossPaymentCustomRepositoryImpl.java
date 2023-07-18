package flab.payment_system.domain.payment.repository.toss;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class TossPaymentCustomRepositoryImpl implements TossPaymentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
}
