package flab.payment_system.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;

}
