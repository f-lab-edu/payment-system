package flab.payment_system.order.service;

import flab.payment_system.domain.order.domain.Order;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.exception.OrderNotExistBadRequestException;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.order.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class OrderServiceIntegrationTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderRepository orderRepository;

	@DisplayName("주문_성공")
	@Test
	public void orderProductSuccess() {
		// given
		long userId = 1;
		OrderProductDto orderProductDto = new OrderProductDto("초코파이", 1, 2, 5000, 5000, 0);
		// when
		long orderId = orderService.orderProduct(orderProductDto, userId);
		Order order = orderRepository.findById(orderId).orElseThrow(
			OrderNotExistBadRequestException::new);
		// then
		Assertions.assertEquals(orderId, order.getOrderId());
		Assertions.assertEquals(orderProductDto.productId(), order.getProductId());
		Assertions.assertEquals(userId, order.getUserId());
		Assertions.assertEquals(orderProductDto.quantity(), order.getQuantity());
	}
}
