package flab.payment_system.domain.payment.repository;

import flab.payment_system.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentCustomRepository {
	Optional<Payment> findByOrderProduct_OrderId(Long orderId);

	Optional<Payment> findByOrderProduct_OrderIdAndPaymentKey(Long orderId, String paymentKey);

}
