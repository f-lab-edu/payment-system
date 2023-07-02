package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.kakao.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.kakao.PaymentReadyDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;


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
		HttpServletRequest request, long userId, long orderId, PaymentPgCompany paymentPgCompany) {
		String requestUrl = request.getRequestURL().toString()
			.replace(request.getRequestURI(), "");

		Payment payment = paymentRepository.save(
			Payment.builder().orderId(orderId).state(PaymentStateConstant.ONGOING.getValue())
				.pgCompany(paymentPgCompany.getValue()).totalAmount(orderProductDto.totalAmount())
				.taxFreeAmount(orderProductDto.taxFreeAmount())
				.installMonth(orderProductDto.installMonth()).build());

		PaymentReadyDto paymentReadyDto = paymentStrategy.createPayment(orderProductDto, userId,
			requestUrl, orderId, payment.getPaymentId());

		paymentRepository.updateTidByPaymentId(
			payment.getPaymentId(), paymentReadyDto.getTid());
		return paymentReadyDto;
	}

	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {

		return paymentStrategy.approvePayment(pgToken, orderId, userId, paymentId);
	}
}

