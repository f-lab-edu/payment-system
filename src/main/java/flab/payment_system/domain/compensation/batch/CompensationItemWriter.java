package flab.payment_system.domain.compensation.batch;

import flab.payment_system.domain.payment.entity.Payment;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Log4j2
@Configuration
@RequiredArgsConstructor
public class CompensationItemWriter {

	@Bean
	@StepScope
	public JpaItemWriter<Payment> paymentItemWriter(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<Payment> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
}
