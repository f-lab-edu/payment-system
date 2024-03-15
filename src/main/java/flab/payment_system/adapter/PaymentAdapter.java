package flab.payment_system.adapter;

import flab.payment_system.domain.order.entity.OrderProduct;
import jakarta.servlet.http.HttpSession;

public interface PaymentAdapter {
	Long getUserId(HttpSession session);

	OrderProduct getOrderProductByOrderId(Long orderId);

	void increaseStock(Long productId, Integer quantity);

	void decreaseStock(Long productId, Integer quantity);
}
