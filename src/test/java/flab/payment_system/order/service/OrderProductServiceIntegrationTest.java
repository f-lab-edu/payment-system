package flab.payment_system.order.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.product.entity.Product;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class OrderProductServiceIntegrationTest {

	private final OrderService orderService;
	private final OrderRepository orderRepository;

	private final DatabaseCleanUp databaseCleanUp;

	@Autowired
	OrderProductServiceIntegrationTest
		(OrderService orderService, OrderRepository orderRepository,
		 DatabaseCleanUp databaseCleanUp) {
		this.orderService = orderService;
		this.orderRepository = orderRepository;
		this.databaseCleanUp = databaseCleanUp;

	}

	@BeforeEach
	void setUp() {
		databaseCleanUp.truncateAllEntity();
	}

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
		assertEquals(orderId, orderProduct.getOrderId());
		assertEquals(orderProductDto.productId(), orderProduct.getProductId());
		assertEquals(userId, orderProduct.getUserId());
		assertEquals(orderProductDto.quantity(), orderProduct.getQuantity());
		assertNotNull(orderProduct);
	}
}
