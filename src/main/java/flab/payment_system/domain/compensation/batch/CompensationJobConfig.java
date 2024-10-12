package flab.payment_system.domain.compensation.batch;


import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.response.toss.Settlement;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;


@Log4j2
@Configuration
@RequiredArgsConstructor
public class CompensationJobConfig {
	private static final int CHUNK = 1000;
	private static final int GRID_SIZE = 10;

	@Bean
	public Job paymentSyncJob(JobRepository jobRepository, Step masterStep) {
		return new JobBuilder("paymentSyncJob", jobRepository)
			.start(masterStep)
			.build();
	}

	@Bean
	public Step masterStep(
		JobRepository jobRepository,
		Partitioner partitioner,
		Step slaveStep,
		TaskExecutor taskExecutor) {
		return new StepBuilder("masterStep", jobRepository)
			.partitioner("slaveStep", partitioner)  // 파티션 설정
			.gridSize(GRID_SIZE)  // 파티션 개수 설정
			.step(slaveStep)  // 각 파티션을 처리할 SlaveStep
			.taskExecutor(taskExecutor)  // 병렬 처리를 위한 TaskExecutor 설정
			.build();
	}

	@Bean
	public Step slaveStep
		(PlatformTransactionManager transactionManager,
		 JobRepository jobRepository,
		 ItemReader<Settlement> paymentStrategyItemReader,
		 ItemProcessor<Settlement, Payment> paymentSettlementItemProcessor,
		 ItemWriter<Payment> paymentItemWriter) {
		return new StepBuilder("slaveStep", jobRepository)
			.<Settlement, Payment>chunk(new SimpleCompletionPolicy(CHUNK))
			.reader(paymentStrategyItemReader)
			.processor(paymentSettlementItemProcessor)
			.writer(paymentItemWriter).transactionManager(transactionManager)
			.build();
	}

	@Bean
	public Partitioner partitioner() {
		return gridSize -> {
			Map<String, ExecutionContext> partitions = new HashMap<>();
			for (int i = 0; i < gridSize; i++) {
				ExecutionContext context = new ExecutionContext();
				context.putInt("partitionNumber", i);  // 각 파티션에 고유 번호 부여
				partitions.put("partition" + i, context);
			}
			return partitions;
		};
	}
}


