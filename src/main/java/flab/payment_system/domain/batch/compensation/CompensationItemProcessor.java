package flab.payment_system.domain.batch.compensation;

import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class CompensationItemProcessor {
	private final PaymentRepository paymentRepository;
	private final OrderRepository orderRepository;

	@Bean
	public ItemProcessor<PaymentCompensationDto, Payment> paymentKakaoItemProcessor() {
		return dto -> processPayment(dto, PaymentPgCompany.KAKAO);
	}

	@Bean
	public ItemProcessor<PaymentCompensationDto, Payment> paymentTossItemProcessor() {
		return dto -> processPayment(dto, PaymentPgCompany.TOSS);
	}

	private Payment processPayment(PaymentCompensationDto dto, PaymentPgCompany pgCompany) {
		return paymentRepository.findByOrderProduct_OrderIdAndPaymentKey(dto.getOrderId(), dto.getPaymentKey())
			.map(payment -> {
				updatePaymentState(payment, dto.getPaymentState());
				return payment;
			}).orElseGet(() -> createPayment(dto, pgCompany));
	}

	private void updatePaymentState(Payment payment, Integer newState) {
		Integer currentState = payment.getState();
		if (!(newState.equals(PaymentStateConstant.APPROVED.getValue()) || newState.equals(PaymentStateConstant.CANCEL.getValue())
			|| newState.equals(PaymentStateConstant.FAIL.getValue())))
			return;
		if (!currentState.equals(newState)) {
			payment.setState(newState);
			log.info("Payment state updated from {} to {} for paymentKey: {}", currentState, newState, payment.getPaymentKey());
		}
	}

	private Payment createPayment(PaymentCompensationDto dto, PaymentPgCompany pgCompany) {
		return orderRepository.findById(dto.getOrderId()).map(orderProduct -> {
			log.info("Creating new payment for orderId: {} with paymentKey: {}", dto.getOrderId(), dto.getPaymentKey());
			return Payment.builder().pgCompany(pgCompany.getValue())
				.paymentKey(dto.getPaymentKey()).orderProduct(orderProduct)
				.state(dto.getPaymentState()).totalAmount(dto.getTotalAmount())
				.taxFreeAmount(dto.getTaxFreeAmount()).installMonth(dto.getInstallMonth()).build();
		}).orElseGet(() -> {
			log.warn("OrderProduct not found for orderId: {}, unable to create payment.", dto.getOrderId());
			return null;
		});

	}
}
