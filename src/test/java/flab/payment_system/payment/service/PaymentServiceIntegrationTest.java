package flab.payment_system.payment.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.toss.PaymentTossDtoImpl;
import flab.payment_system.domain.payment.service.PaymentService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class PaymentServiceIntegrationTest {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private DatabaseCleanUp databaseCleanUp;

	private static String requestUrl;

	@BeforeAll
	public static void setUp() {
		requestUrl = "http://localhost:8080";
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
			PaymentPgCompany pgCompany = PaymentPgCompany.KAKAO;
			OrderProductDto orderProductDto = new OrderProductDto("초코파이", 1L, 2, 5000, 5000, 0);

			// when
			Long orderId = orderService.orderProduct(orderProductDto, userId);
			paymentService.setStrategy(pgCompany);
			PaymentKakaoReadyDtoImpl paymentReadyDto = (PaymentKakaoReadyDtoImpl) paymentService.createPayment(
				orderProductDto, requestUrl,
				userId, orderId, pgCompany);
			Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

			// then
			Assertions.assertEquals(paymentReadyDto.getPaymentId(), payment.getPaymentId());
			Assertions.assertNotNull(payment);
		}

		@DisplayName("pg_toss")
		@Test
		public void createTossPaymentSuccess() {
			// given
			Long userId = 1L;
			PaymentPgCompany pgCompany = PaymentPgCompany.TOSS;
			OrderProductDto orderProductDto = new OrderProductDto("초코파이", 1L, 2, 5000, 5000, 0);

			// when
			Long orderId = orderService.orderProduct(orderProductDto, userId);
			paymentService.setStrategy(pgCompany);
			PaymentTossDtoImpl paymentReadyDto = (PaymentTossDtoImpl) paymentService.createPayment(
				orderProductDto, requestUrl,
				userId, orderId, pgCompany);
			Payment payment = paymentRepository.findByOrderId(orderId).orElse(null);

			// then
			Assertions.assertEquals(paymentReadyDto.getPaymentId(), payment.getPaymentId());
			Assertions.assertNotNull(payment);
		}
	}
}
