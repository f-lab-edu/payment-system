package flab.payment_system.domain.compensation.batch;

import java.util.Arrays;

import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import lombok.RequiredArgsConstructor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CompensationJobConfig {
	private final CompensationStepExecutionListener compensationStepExecutionListener;

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
		ItemWriter<Payment> compositeKakaoItemWriter) {
		return new StepBuilder("paymentKakaoSyncStep", jobRepository)
			.<PaymentCompensationDto, Payment>chunk(new SimpleCompletionPolicy(1000))
			.reader(paymentKakaoItemReader)
			.processor(paymentKakaoItemProcessor)
			.writer(compositeKakaoItemWriter)
			.transactionManager(transactionManager).listener(compensationStepExecutionListener)
			.build();
	}

	@Bean
	public Step paymentTossSyncStep(
		JobRepository jobRepository,
		PlatformTransactionManager transactionManager,
		ItemReader<PaymentCompensationDto> paymentTossItemReader,
		ItemProcessor<PaymentCompensationDto, Payment> paymentTossItemProcessor,
		ItemWriter<Payment> compositeTossItemWriter) {
		return new StepBuilder("paymentTossSyncStep", jobRepository)
			.<PaymentCompensationDto, Payment>chunk(new SimpleCompletionPolicy(1000))
			.reader(paymentTossItemReader)
			.processor(paymentTossItemProcessor)
			.writer(compositeTossItemWriter)
			.transactionManager(transactionManager).listener(compensationStepExecutionListener)
			.build();
	}

	@Bean
	public CompositeItemWriter<Payment> compositeKakaoItemWriter(ItemWriter<Payment> paymentItemWriter,
		ItemWriter<Payment> customFileKakaoItemWriter) {
		CompositeItemWriter<Payment> compositeItemWriter = new CompositeItemWriter<>();
		compositeItemWriter.setDelegates(Arrays.asList(paymentItemWriter, customFileKakaoItemWriter));
		return compositeItemWriter;
	}

	@Bean
	public CompositeItemWriter<Payment> compositeTossItemWriter(ItemWriter<Payment> paymentItemWriter,
		ItemWriter<Payment> customFileTossItemWriter) {
		CompositeItemWriter<Payment> compositeItemWriter = new CompositeItemWriter<>();
		compositeItemWriter.setDelegates(Arrays.asList(paymentItemWriter, customFileTossItemWriter));
		return compositeItemWriter;
	}
}
