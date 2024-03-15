package flab.payment_system.payment.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.order.dto.OrderDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.toss.PaymentTossDtoImpl;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.repository.ProductRepository;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class PaymentServiceIntegrationTest {
	private final PaymentService paymentService;
	private final OrderService orderService;
	private final PaymentRepository paymentRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final DatabaseCleanUp databaseCleanUp;
	private final String requestUrl;
	private Product product;

	@Autowired
	PaymentServiceIntegrationTest(PaymentService paymentService, OrderService orderService, ProductRepository productRepository, UserRepository userRepository, @Value("${test-url}") String requestUrl, PaymentRepository paymentRepository, DatabaseCleanUp databaseCleanUp) {
		this.paymentService = paymentService;
		this.orderService = orderService;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.requestUrl = requestUrl;
		this.paymentRepository = paymentRepository;
		this.databaseCleanUp = databaseCleanUp;

	}

	@BeforeEach
	void setUp() {
		databaseCleanUp.truncateAllEntity();
		product = new Product();
		product.setName("초코파이");
		product.setPrice(5000);
		product.setStock(100);
		User user = User.builder().email("test@gmail.com").password("1234").build();
		userRepository.save(user);
		productRepository.save(product);
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllEntity();
	}

	@Nested
	@DisplayName("결제생성_성공")
	public class createPaymentTest {
		@DisplayName("pg_kakao")
		@Test
		public void createKakaoPaymentSuccess() {
			// given
			Long userId = 1L;
			Integer quantity = 2;
			Integer totalAmount = 5000;
			Integer totalFreeAmount = 500;
			Integer installMonth = 3;


			PaymentPgCompany pgCompany = PaymentPgCompany.KAKAO;
			OrderProductDto orderProductDto = new OrderProductDto(product.getProductId(), quantity);
			OrderDto orderDto = orderService.orderProduct(orderProductDto, userId);
			PaymentCreateDto paymentCreateDto = new PaymentCreateDto(orderDto.orderId(), product.getName(), product.getProductId(), quantity, totalAmount, totalFreeAmount, installMonth);

			// when
			paymentService.setStrategy(pgCompany);
			PaymentKakaoReadyDtoImpl paymentReadyDto = (PaymentKakaoReadyDtoImpl) paymentService.createPayment(paymentCreateDto, requestUrl, userId, pgCompany);
			Payment payment = paymentRepository.findById(paymentReadyDto.getPaymentId()).orElseThrow(PaymentNotExistBadRequestException::new);

			// then
			assertEquals(paymentReadyDto.getPaymentId(), payment.getPaymentId());
			assertNotNull(payment);
		}

		@DisplayName("pg_toss")
		@Test
		public void createTossPaymentSuccess() {
			// given
			Long userId = 1L;
			Integer quantity = 2;
			Integer totalAmount = 5000;
			Integer totalFreeAmount = 500;
			Integer installMonth = 3;


			PaymentPgCompany pgCompany = PaymentPgCompany.TOSS;
			OrderProductDto orderProductDto = new OrderProductDto(product.getProductId(), quantity);
			OrderDto orderDto = orderService.orderProduct(orderProductDto, userId);
			PaymentCreateDto paymentCreateDto = new PaymentCreateDto(orderDto.orderId(), product.getName(), product.getProductId(), quantity, totalAmount, totalFreeAmount, installMonth);

			// when
			paymentService.setStrategy(pgCompany);
			PaymentTossDtoImpl paymentReadyDto = (PaymentTossDtoImpl) paymentService.createPayment(paymentCreateDto, requestUrl, userId, pgCompany);
			Payment payment = paymentRepository.findById(paymentReadyDto.getPaymentId()).orElseThrow(PaymentNotExistBadRequestException::new);


			// then
			assertEquals(paymentReadyDto.getPaymentId(), payment.getPaymentId());
			assertNotNull(payment);
		}
	}
}
