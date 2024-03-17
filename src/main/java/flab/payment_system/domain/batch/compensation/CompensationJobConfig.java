package flab.payment_system.domain.batch.compensation;

import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class CompensationJobConfig {


	@Bean
	public Job paymentSyncJob(JobRepository jobRepository, Step paymentKakaoSyncStep, Step paymentTossSyncStep) {
		return new JobBuilder("paymentSyncJob", jobRepository)
			.start(paymentKakaoSyncStep).next(paymentTossSyncStep)
			.build();
	}

	@Bean
	public Step paymentKakaoSyncStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<PaymentCompensationDto> paymentKakaoItemReader,
		ItemProcessor<PaymentCompensationDto, Payment> paymentKakaoItemProcessor,
		ItemWriter<Payment> paymentItemWriter) {

		return new StepBuilder("paymentKakaoSyncStep", jobRepository)
			.<PaymentCompensationDto, Payment>chunk(1000)
			.reader(paymentKakaoItemReader)
			.processor(paymentKakaoItemProcessor)
			.writer(paymentItemWriter)
			.transactionManager(transactionManager)
			.build();
	}

	@Bean
	public Step paymentTossSyncStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<PaymentCompensationDto> paymentTossItemReader,
		ItemProcessor<PaymentCompensationDto, Payment> paymentTossItemProcessor,
		ItemWriter<Payment> paymentItemWriter) {

		return new StepBuilder("paymentTossSyncStep", jobRepository)
			.<PaymentCompensationDto, Payment>chunk(1000)
			.reader(paymentTossItemReader)
			.processor(paymentTossItemProcessor)
			.writer(paymentItemWriter)
			.transactionManager(transactionManager)
			.build();
	}
}
