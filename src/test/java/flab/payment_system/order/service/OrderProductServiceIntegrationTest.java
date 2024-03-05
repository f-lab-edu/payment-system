package flab.payment_system.order.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.order.service.OrderService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class OrderProductServiceIntegrationTest {

	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllEntity();
	}


	@DisplayName("주문_성공")
	@Test
	public void orderProductSuccess() {
		// given
		Long userId = 1L;
		OrderProductDto orderProductDto = new OrderProductDto("초코파이", 1L, 2, 5000, 5000, 0);
		// when
		Long orderId = orderService.orderProduct(orderProductDto, userId);
		OrderProduct orderProduct = orderRepository.findById(orderId).orElse(null);
		// then
		Assertions.assertEquals(orderId, orderProduct.getOrderId());
		Assertions.assertEquals(orderProductDto.productId(), orderProduct.getProductId());
		Assertions.assertEquals(userId, orderProduct.getUserId());
		Assertions.assertEquals(orderProductDto.quantity(), orderProduct.getQuantity());
		Assertions.assertNotNull(orderProduct);
	}
}
