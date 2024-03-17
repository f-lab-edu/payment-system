package flab.payment_system.domain.payment.repository.toss;

import flab.payment_system.domain.payment.entity.toss.TossPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TossPaymentRepository extends JpaRepository<TossPayment, Long>,
	TossPaymentCustomRepository {

}
