package flab.payment_system.adapter;

import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.redisson.service.RedissonLockService;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class PaymentAdapterImpl implements PaymentAdapter {

	private final UserService userService;
	private final OrderService orderService;
	private final RedissonLockService redissonLockService;

	@Override
	public Long getUserId(HttpSession session) {
		return userService.getUserId(session);
	}

	@Override
	public OrderProduct getOrderProductByOrderId(Long orderId) {
		return orderService.getOrderProductByOrderId(orderId);
	}

	@Override
	public void increaseStock(Long productId, Integer quantity) {
		redissonLockService.increaseStock(productId, quantity);
	}

	@Override
	public void decreaseStock(Long productId, Integer quantity) {
		redissonLockService.decreaseStock(productId, quantity);
	}

}
