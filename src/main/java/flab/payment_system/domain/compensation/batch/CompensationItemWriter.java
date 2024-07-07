package flab.payment_system.domain.compensation.batch;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import flab.payment_system.domain.compensation.batch.correction.CorrectionFileWriter;
import flab.payment_system.domain.compensation.enums.PGManageName;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import flab.payment_system.domain.payment.entity.Payment;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CompensationItemWriter {

	private final CorrectionFileWriter correctionFileWriter;

	@SuppressWarnings("unchecked")
	private <T> List<T> getExecutionContextList(ExecutionContext executionContext, String key) {
		return (List<T>)executionContext.get(key);
	}

	private ExecutionContext getStepExecutionContext() {
		return Objects.requireNonNull(StepSynchronizationManager.getContext()).getStepExecution().getExecutionContext();
	}

	@Bean
	@StepScope
	public JpaItemWriter<Payment> paymentItemWriter(EntityManagerFactory entityManagerFactory) {
		JpaItemWriter<Payment> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

	@Bean
	@StepScope
	public ItemWriter<Payment> customFileKakaoItemWriter() {
		return chunk -> {
			String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

			List<PaymentCompensationDto> notFoundDtos = getExecutionContextList(getStepExecutionContext(),
				PGManageName.KAKAO.getBatchContextNotFoundName());
			List<Payment> inconsistentPayments = getExecutionContextList(getStepExecutionContext(),
				PGManageName.KAKAO.getBatchContextInconsistentName());

			correctionFileWriter.writeNotFoundPaymentForCorrection(notFoundDtos,
				PGManageName.KAKAO.getNotFoundFileName() + timestamp + ".csv");
			correctionFileWriter.writeInconsistentPaymentForCorrection(inconsistentPayments,
				PGManageName.KAKAO.getInconsistentFileName() + timestamp + ".csv");

			notFoundDtos.clear();
			inconsistentPayments.clear();

		};
	}

	@Bean
	@StepScope
	public ItemWriter<Payment> customFileTossItemWriter() {
		return chunk -> {
			String timestamp = OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmm"));

			List<PaymentCompensationDto> notFoundDtos = getExecutionContextList(getStepExecutionContext(),
				PGManageName.TOSS.getBatchContextNotFoundName());
			List<Payment> inconsistentPayments = getExecutionContextList(getStepExecutionContext(),
				PGManageName.TOSS.getBatchContextInconsistentName());

			correctionFileWriter.writeNotFoundPaymentForCorrection(notFoundDtos,
				PGManageName.TOSS.getNotFoundFileName() + timestamp + ".csv");
			correctionFileWriter.writeInconsistentPaymentForCorrection(inconsistentPayments,
				PGManageName.TOSS.getInconsistentFileName() + timestamp + ".csv");

			notFoundDtos.clear();
			inconsistentPayments.clear();

		};
	}
}
