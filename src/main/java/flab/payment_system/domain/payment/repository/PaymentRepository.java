package flab.payment_system.domain.payment.repository;

import flab.payment_system.domain.payment.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long>, PaymentCustomRepository {

}
