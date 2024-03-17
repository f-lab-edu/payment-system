package flab.payment_system.domain.batch.compensation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class CompensationConfig {
	private final JobLauncher jobLauncher;

	private final Job paymentSyncJob;

	@Scheduled(fixedDelay = 600000)
	public void runJob() {
		try {
			JobParameters parameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(paymentSyncJob, parameters);
		} catch (Exception e) {
			log.error("batch exception : {}", e.getMessage());
			System.out.println(e.getMessage());
		}
	}
}
