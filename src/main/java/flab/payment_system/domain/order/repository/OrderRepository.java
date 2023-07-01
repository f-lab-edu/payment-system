package flab.payment_system.domain.order.repository;

import flab.payment_system.domain.order.domain.Order;
import flab.payment_system.domain.payment.domain.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderCustomRepository {

	Optional<Order> findByOrderId(Long orderId);

}
