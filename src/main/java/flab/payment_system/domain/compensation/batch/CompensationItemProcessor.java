package flab.payment_system.domain.compensation.batch;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import flab.payment_system.domain.compensation.enums.PGManageName;
import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CompensationItemProcessor {
	private final PaymentRepository paymentRepository;

	@Bean
	@StepScope
	public ItemProcessor<PaymentCompensationDto, Payment> paymentKakaoItemProcessor() {
		return dto -> processPayment(dto, PaymentPgCompany.KAKAO);
	}

	@Bean
	@StepScope
	public ItemProcessor<PaymentCompensationDto, Payment> paymentTossItemProcessor() {
		return dto -> processPayment(dto, PaymentPgCompany.TOSS);
	}

	private Payment processPayment(PaymentCompensationDto dto, PaymentPgCompany pgCompany) {
		Optional<Payment> optionalPayment = paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(dto.getOrderId(),
			dto.getPaymentKey());
		if (optionalPayment.isPresent()) {
			Payment payment = optionalPayment.get();
			updatePaymentState(payment, dto.getPaymentState(), pgCompany);
			return payment;
		} else {
			PGManageName pgManageName = PGManageName.valueOf(pgCompany.getName().toUpperCase());
			List<PaymentCompensationDto> notFoundDtos = getExecutionContextList(getStepExecutionContext(),
				pgManageName.getBatchContextNotFoundName());
			notFoundDtos.add(dto);
			log.warn("Payment not found for orderId: {}, paymentKey: {}. Correction file entry created.",
				dto.getOrderId(), dto.getPaymentKey());
			return null;
		}
	}

	private void updatePaymentState(Payment payment, Integer pgState, PaymentPgCompany pgCompany) {
		PGManageName pgManageName = PGManageName.valueOf(pgCompany.getName().toUpperCase());
		List<Payment> inconsistentPayments = getExecutionContextList(getStepExecutionContext(),
			pgManageName.getBatchContextNotFoundName());
		Integer dbState = payment.getState();
		if (pgState.equals(PaymentStateConstant.APPROVED.getValue())) {
			if (!dbState.equals(PaymentStateConstant.APPROVED.getValue())) {
				payment.setState(pgState);
				log.error("Payment state inconsistent dbState {} and pgState {} for paymentKey: {}", dbState, pgState,
					payment.getPaymentKey());
			}
		} else if (pgState.equals(PaymentStateConstant.FAIL.getValue()) || pgState.equals(
			PaymentStateConstant.CANCEL.getValue())) {
			if (dbState.equals(PaymentStateConstant.ONGOING.getValue())) {
				payment.setState(pgState);
				log.info("Payment state updated from {} to {} for paymentKey: {}", dbState, pgState,
					payment.getPaymentKey());
			} else if (dbState.equals(PaymentStateConstant.APPROVED.getValue())) {
				inconsistentPayments.add(payment);
				log.error("Payment state inconsistent dbState {} and pgState {} for paymentKey: {}", dbState, pgState,
					payment.getPaymentKey());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getExecutionContextList(ExecutionContext executionContext, String key) {
		return (List<T>)executionContext.get(key);
	}

	private ExecutionContext getStepExecutionContext() {
		return Objects.requireNonNull(StepSynchronizationManager.getContext()).getStepExecution().getExecutionContext();
	}
}
