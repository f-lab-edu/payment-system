package flab.payment_system.domain.order.repository;

import flab.payment_system.domain.order.domain.OrderProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderProduct, Long>, OrderCustomRepository {

	Optional<OrderProduct> findById(Long orderId);

}
