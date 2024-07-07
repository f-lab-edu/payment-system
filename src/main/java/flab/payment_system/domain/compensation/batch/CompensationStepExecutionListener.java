package flab.payment_system.domain.compensation.batch;

import java.util.ArrayList;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import flab.payment_system.domain.compensation.enums.PGManageName;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import flab.payment_system.domain.payment.entity.Payment;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CompensationStepExecutionListener implements StepExecutionListener {

	@Override
	@BeforeStep
	public void beforeStep(StepExecution stepExecution) {
		ExecutionContext executionContext = stepExecution.getExecutionContext();
		executionContext.put(PGManageName.KAKAO.getBatchContextNotFoundName(), new ArrayList<PaymentCompensationDto>());
		executionContext.put(PGManageName.TOSS.getBatchContextNotFoundName(), new ArrayList<PaymentCompensationDto>());
		executionContext.put(PGManageName.KAKAO.getBatchContextInconsistentName(), new ArrayList<Payment>());
		executionContext.put(PGManageName.TOSS.getBatchContextInconsistentName(), new ArrayList<Payment>());
		log.info("Initialized ExecutionContext in beforeStep");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("ExecutionContext in afterStep: {}", stepExecution.getExecutionContext());
		return ExitStatus.COMPLETED;
	}

	@Bean
	public CompensationStepExecutionListener promotionListener() {
		return new CompensationStepExecutionListener();
	}
}
