package flab.payment_system.batch.compensation;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.*;

import flab.payment_system.adapter.PaymentAdapter;
import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.response.toss.Settlement;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.repository.ProductRepository;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Component;

@Import(value = BatchIntegrationTest.PaymentServiceTestConfig.class)
@Profile("test")
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class BatchIntegrationTest {
	@TestConfiguration
	static class PaymentServiceTestConfig {
		@Autowired
		private Map<PaymentPgCompany, PaymentStrategy> paymentStrategies;

		@Autowired
		private PaymentAdapter paymentAdapter;

		@Autowired
		private PaymentRepository paymentRepository;


		@Bean
		@Primary
		public PaymentService paymentServiceStub() {
			return new PaymentServiceStub(paymentStrategies, paymentRepository, paymentAdapter);
		}
	}

	// 내부 Stub
	private static class PaymentServiceStub extends PaymentService {

		private Settlement[] stubSettlementList;

		public PaymentServiceStub(Map<PaymentPgCompany, PaymentStrategy> paymentStrategies, PaymentRepository paymentRepository, PaymentAdapter paymentAdapter) {
			super(paymentStrategies, paymentRepository, paymentAdapter);
		}

		public void setStubSettlementList(Settlement[] stubSettlementList) {
			this.stubSettlementList = stubSettlementList;
		}

		@Override
		public Settlement[] getSettlementList(PaymentPgCompany pgCompany) {
			System.out.println("hello:"+stubSettlementList[0]);
			return stubSettlementList;
		}
	}

	@Autowired
	private JobLauncher jobLauncher;

	@Autowired
	private Job paymentSyncJob;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private OrderRepository orderRepository;

	private Payment payment;
	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PaymentService paymentService;

	@BeforeEach
	void setup() {
		Product product = new Product();
		User user = User.builder().email("testuser@example.com").build();
		OrderProduct orderProduct = OrderProduct.builder().user(user).product(product).quantity(1).build();
		payment = Payment.builder().orderProduct(orderProduct).state(PaymentStateConstant.FAIL.getValue()).pgCompany(PaymentPgCompany.TOSS.getValue()).totalAmount(5000).taxFreeAmount(0).installMonth(0).paymentKey("paymentKey1").build();

		productRepository.save(product);
		userRepository.save(user);
		orderRepository.save(orderProduct);
		paymentRepository.save(payment);

		PaymentServiceStub paymentServiceStub = (PaymentServiceStub) paymentService;  // 주입된 paymentService를 Stub으로 캐스팅
		paymentServiceStub.setStubSettlementList(createStubSettlements());
	}

	@Test
	void testPaymentSyncJob() throws Exception {
		// 배치 실행
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();

		JobExecution jobExecution = jobLauncher.run(paymentSyncJob, jobParameters);

		// 배치 작업이 성공적으로 완료되었는지 확인
		assertThat(jobExecution.getStatus().isUnsuccessful()).isFalse();
		assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");

		// 배치 작업 후 Payment 상태가 APPROVED로 변경되었는지 확인
		Optional<Payment> updatedPayment = paymentRepository.findById(payment.getPaymentId());
		assertThat(updatedPayment).isPresent();
		assertThat(updatedPayment.get().getState()).isEqualTo(PaymentStateConstant.APPROVED.getValue());
	}

	private Settlement[] createStubSettlements() {
		List<Settlement> settlements = new ArrayList<>();

		// 첫 번째 가짜 Settlement 데이터
		Settlement stubSettlement = new Settlement();
		stubSettlement.setOrderId(String.valueOf(payment.getOrderProduct().getOrderId()));
		stubSettlement.setPaymentKey(payment.getPaymentKey());
		stubSettlement.setAmount(5000);
		stubSettlement.setApprovedAt("2024-10-12T12:00:00");
		settlements.add(stubSettlement);

		return settlements.toArray(new Settlement[0]);
	}
}
