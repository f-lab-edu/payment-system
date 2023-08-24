package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.exception.OrderNotExistBadRequestException;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.kakao.PaymentStrategyKaKaoService;
import flab.payment_system.domain.payment.service.toss.PaymentStrategyTossService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;


@Service
@RequiredArgsConstructor
@RequestScope
public class PaymentService {

	private PaymentStrategy paymentStrategy;
	private final ApplicationContext applicationContext;
	private final PaymentRepository paymentRepository;

	public void setStrategy(PaymentPgCompany paymentPgCompany) {
		if (paymentPgCompany == PaymentPgCompany.TOSS) {
			this.paymentStrategy = applicationContext.getBean(PaymentStrategyTossService.class);
		}
		if (paymentPgCompany == PaymentPgCompany.KAKAO) {
			this.paymentStrategy = applicationContext.getBean(PaymentStrategyKaKaoService.class);
		}
	}

	public PaymentReadyDto createPayment(OrderProductDto orderProductDto,
		String requestUrl, long userId, long orderId, PaymentPgCompany paymentPgCompany) {

		Payment payment = paymentRepository.save(
			Payment.builder().orderId(orderId).state(PaymentStateConstant.ONGOING.getValue())
				.pgCompany(paymentPgCompany.getValue()).totalAmount(orderProductDto.totalAmount())
				.taxFreeAmount(orderProductDto.taxFreeAmount())
				.installMonth(orderProductDto.installMonth()).build());

		PaymentReadyDto paymentReadyDto = paymentStrategy.createPayment(orderProductDto, userId,
			requestUrl, orderId, payment.getPaymentId(), orderProductDto.productId());
		paymentReadyDto.setPaymentId(payment.getPaymentId());

		paymentRepository.updatePaymentKeyByPaymentId(
			payment.getPaymentId(), paymentReadyDto.getPaymentKey());

		return paymentReadyDto;
	}

	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {

		PaymentApprovalDto paymentApprovalDto = paymentStrategy.approvePayment(pgToken, orderId,
			userId, paymentId);

		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.APPROVED.getValue());

		return paymentApprovalDto;
	}

	public void cancelPayment(Long paymentId) {
		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.CANCEL.getValue());
	}

	public void failPayment(Long paymentId) {
		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.FAIL.getValue());
	}

	public PaymentCancelDto orderCancel(OrderCancelDto orderCancelDto) {
		PaymentCancelDto paymentCancelDto = paymentStrategy.cancelPayment(orderCancelDto);

		paymentRepository.updatePaymentStateByOrderId(orderCancelDto.orderId(),
			PaymentStateConstant.CANCEL.getValue());
		return paymentCancelDto;
	}

	public PaymentOrderDetailDto getOrderDetail(String paymentKey) {
		return paymentStrategy.getOrderDetail(paymentKey);
	}
}

