package flab.payment_system.batch.compensation;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.repository.ProductRepository;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.repository.UserRepository;

@Profile("test")
@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class BatchIntegrationTest {
	private final DatabaseCleanUp databaseCleanUp;
	private final JobLauncher jobLauncher;
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderProductRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;
	private final Job paymentSyncJob;

	private static final String KAKAO_FILE_NAME = "payment_kakao.csv";
	private static final String TOSS_FILE_NAME = "payment_toss.csv";
	private String CORRECTION_FILE_NAME;

	private Product product;
	private Long userId;
	private final String targetPath;

	private final String testFilePath;

	@Autowired
	BatchIntegrationTest(DatabaseCleanUp databaseCleanUp, JobLauncher jobLauncher,
		PaymentRepository paymentRepository, OrderRepository orderProductRepository, UserRepository userRepository,
		ProductRepository productRepository, Job paymentSyncJob,
		@Value("${target-path}") String targetPath, @Value("${test-file-path}") String testFilePath) {
		this.databaseCleanUp = databaseCleanUp;
		this.jobLauncher = jobLauncher;
		this.paymentRepository = paymentRepository;
		this.orderProductRepository = orderProductRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
		this.paymentSyncJob = paymentSyncJob;
		this.targetPath = targetPath;
		this.testFilePath = testFilePath;
	}

	@BeforeEach
	public void setUp() {
		databaseCleanUp.truncateAllEntity();

		product = new Product();
		product.setName("초코파이");
		product.setPrice(5000);
		product.setStock(100);
		User user = User.builder().email("test@gmail.com").password("1234").build();
		userRepository.save(user);
		productRepository.save(product);

		userId = 1L;

		createTestData();
		generateCsvData(KAKAO_FILE_NAME);
		generateCsvData(TOSS_FILE_NAME);
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllEntity();
		deleteFile(KAKAO_FILE_NAME);
		deleteFile(TOSS_FILE_NAME);
	}

	@Test
	@DisplayName("Payment Sync Job 테스트")
	public void PaymentSyncJob() throws Exception {
		// given
		JobParameters jobParameters = new JobParametersBuilder()
			.addLong("time", System.currentTimeMillis())
			.toJobParameters();

		// when
		JobExecution jobExecution = jobLauncher.run(paymentSyncJob, jobParameters);

		// then
		assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

		// 보정이 됐는지 추가검증
		// 1. PG사 파일에는 결제 성공인데 해당 서버에는 결제 성공 상태가 아닌 경우
		Payment paymentNotApproved = paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(1L, "Key123")
			.orElseThrow();
		assertThat(paymentNotApproved.getState()).isEqualTo(PaymentStateConstant.APPROVED.getValue());

		// 2. PG사 파일에는 결제 취소인데 해당 서버에는 진행중인 경우
		Payment paymentCancelled = paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(2L, "Key124")
			.orElseThrow();
		assertThat(paymentCancelled.getState()).isEqualTo(PaymentStateConstant.CANCEL.getValue());

		// 3. PG사 파일에는 결제 실패인데 해당 서버에는 진행중인 경우
		Payment paymentFailed = paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(3L, "Key125")
			.orElseThrow();
		assertThat(paymentFailed.getState()).isEqualTo(PaymentStateConstant.FAIL.getValue());

		// 4. PG사 파일에는 결제 취소인데 해당 서버에는 성공으로 돼있는 경우
		File correctionFile = new File(CORRECTION_FILE_NAME);
		assertThat(correctionFile.exists()).isTrue();
		List<String> lines = Files.readAllLines(correctionFile.toPath());
		assertThat(lines.stream().anyMatch(line -> line.contains("Key126"))).isTrue();

		// 5. PG사 파일에는 결제 실패인데 해당 서버에는 성공으로 돼있는 경우
		assertThat(lines.stream().anyMatch(line -> line.contains("Key127"))).isTrue();
	}

	private void createTestData() {
		// 결제 상태가 성공이 아닌 데이터
		OrderProduct orderProduct1 = orderProductRepository.save(
			new OrderProduct(userRepository.findById(userId).orElseThrow(), product, 10));
		Payment payment1 = Payment.builder()
			.orderProduct(orderProduct1)
			.paymentKey("Key123")
			.totalAmount(50000)
			.taxFreeAmount(0)
			.installMonth(3)
			.state(PaymentStateConstant.ONGOING.getValue()) // 상태를 진행중으로 설정
			.pgCompany(PaymentPgCompany.KAKAO.getValue())
			.build();
		paymentRepository.save(payment1);

		// 결제 상태가 진행중인 데이터 (결제 취소 케이스)
		OrderProduct orderProduct2 = orderProductRepository.save(
			new OrderProduct(userRepository.findById(userId).orElseThrow(), product, 10));
		Payment payment2 = Payment.builder()
			.orderProduct(orderProduct2)
			.paymentKey("Key124")
			.totalAmount(75000)
			.taxFreeAmount(5000)
			.installMonth(0)
			.state(PaymentStateConstant.ONGOING.getValue()) // 상태를 진행중으로 설정
			.pgCompany(PaymentPgCompany.KAKAO.getValue())
			.build();
		paymentRepository.save(payment2);

		// 결제 상태가 진행중인 데이터 (결제 실패 케이스)
		OrderProduct orderProduct3 = orderProductRepository.save(
			new OrderProduct(userRepository.findById(userId).orElseThrow(), product, 10));
		Payment payment3 = Payment.builder()
			.orderProduct(orderProduct3)
			.paymentKey("Key125")
			.totalAmount(25000)
			.taxFreeAmount(0)
			.installMonth(6)
			.state(PaymentStateConstant.ONGOING.getValue()) // 상태를 진행중으로 설정
			.pgCompany(PaymentPgCompany.KAKAO.getValue())
			.build();
		paymentRepository.save(payment3);

		// 결제 상태가 성공인 데이터 (결제 취소 케이스)
		OrderProduct orderProduct4 = orderProductRepository.save(
			new OrderProduct(userRepository.findById(userId).orElseThrow(), product, 10));
		Payment payment4 = Payment.builder()
			.orderProduct(orderProduct4)
			.paymentKey("Key126")
			.totalAmount(30000)
			.taxFreeAmount(0)
			.installMonth(12)
			.state(PaymentStateConstant.APPROVED.getValue()) // 상태를 성공으로 설정
			.pgCompany(PaymentPgCompany.KAKAO.getValue())
			.build();
		paymentRepository.save(payment4);

		// 결제 상태가 성공인 데이터 (결제 실패 케이스)
		OrderProduct orderProduct5 = orderProductRepository.save(
			new OrderProduct(userRepository.findById(userId).orElseThrow(), product, 10));
		Payment payment5 = Payment.builder()
			.orderProduct(orderProduct5)
			.paymentKey("Key127")
			.totalAmount(45000)
			.taxFreeAmount(4500)
			.installMonth(0)
			.state(PaymentStateConstant.APPROVED.getValue()) // 상태를 성공으로 설정
			.pgCompany(PaymentPgCompany.KAKAO.getValue())
			.build();
		paymentRepository.save(payment5);
	}

	private void generateCsvData(String fileName) {
		CORRECTION_FILE_NAME = testFilePath + fileName;
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(CORRECTION_FILE_NAME))) {
			writer.write("orderId,paymentKey,totalAmount,paymentState,taxFreeAmount,installMonth\n");

			// PG사 파일에는 결제 성공
			writer.write(String.format("%d,%s,%d,%d,%d,%d%n",
				1, "Key123", 50000, PaymentStateConstant.APPROVED.getValue(), 0, 3));

			// PG사 파일에는 결제 취소
			writer.write(String.format("%d,%s,%d,%d,%d,%d%n",
				2, "Key124", 75000, PaymentStateConstant.CANCEL.getValue(), 5000, 0));

			// PG사 파일에는 결제 실패
			writer.write(String.format("%d,%s,%d,%d,%d,%d%n",
				3, "Key125", 25000, PaymentStateConstant.FAIL.getValue(), 0, 6));

			// PG사 파일에는 결제 취소
			writer.write(String.format("%d,%s,%d,%d,%d,%d%n",
				4, "Key126", 30000, PaymentStateConstant.CANCEL.getValue(), 0, 12));

			// PG사 파일에는 결제 실패
			writer.write(String.format("%d,%s,%d,%d,%d,%d%n",
				5, "Key127", 45000, PaymentStateConstant.FAIL.getValue(), 4500, 0));

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void deleteFile(String fileName) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFilePath + fileName))) {
			writer.write("");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
