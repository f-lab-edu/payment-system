package flab.payment_system.domain.payment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.payment_system.domain.payment.domain.QPayment;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class PaymentCustomRepositoryImpl implements PaymentCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QPayment payment = QPayment.payment;

	@Override
	@Transactional
	@Modifying(clearAutomatically = true)
	public long updatePaymentStateByOrderId(long orderId) {
		return jpaQueryFactory.update(payment)
			.set(payment.state, PaymentStateConstant.APPROVED.getValue())
			.where(payment.orderId.eq(orderId)).execute();
	}

	@Override
	@Transactional
	@Modifying(clearAutomatically = true)
	public long updateTidByPaymentId(long paymentId, String tid) {
		return jpaQueryFactory.update(payment).set(payment.tid, tid)
			.where(payment.paymentId.eq(paymentId)).execute();
	}
}
