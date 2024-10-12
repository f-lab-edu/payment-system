package flab.payment_system.domain.compensation.batch;

import java.util.*;

import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.toss.Settlement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.batch.core.configuration.annotation.StepScope;
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
	public ItemProcessor<Settlement, Payment> paymentSettlementItemProcessor() {
		return settlement -> processPaymentFromSettlement(settlement);
	}

	private Payment processPaymentFromSettlement(Settlement settlement) {

		Optional<Payment> optionalPayment = paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(
			Long.valueOf(settlement.getOrderId()), settlement.getPaymentKey());

		if (optionalPayment.isPresent()) {
			Payment payment = optionalPayment.get();
			updatePaymentState(payment);  // Settlement 정보가 있으므로 무조건 APPROVED 처리
			return payment;
		} else {
			// 일치하는 Payment가 없는 경우 경고 로그 출력
			log.warn("No matching payment found for orderId: {}, paymentKey: {}", settlement.getOrderId(),
				settlement.getPaymentKey());
			return null;
		}
	}

	private void updatePaymentState(Payment payment) {
		Integer dbState = payment.getState();

		if (!dbState.equals(PaymentStateConstant.APPROVED.getValue())) {
			payment.setState(PaymentStateConstant.APPROVED.getValue());
			paymentRepository.save(payment);
			log.info("Payment state updated to APPROVED for paymentKey: {}", payment.getPaymentKey());
		} else {
			log.info("Payment already APPROVED for paymentKey: {}", payment.getPaymentKey());
		}
	}
}
