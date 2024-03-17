package flab.payment_system.domain.batch.compensation;

import flab.payment_system.domain.payment.domain.Payment;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CompensationItemWrite {
	@Bean
	public JpaItemWriter<Payment> paymentItemWriter(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<Payment> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
}
