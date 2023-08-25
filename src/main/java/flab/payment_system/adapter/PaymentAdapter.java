package flab.payment_system.adapter;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAdapter {

	private final PaymentService paymentService;

	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		return paymentService.approvePayment(pgToken, orderId, userId, paymentId);
	}

	public void setStrategy(PaymentPgCompany pgCompany) {
		paymentService.setStrategy(pgCompany);
	}

	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, String requestUrl,
		long userId, long orderId, PaymentPgCompany pgCompany) {
		return paymentService.createPayment(orderProductDto, requestUrl, userId, orderId,
			pgCompany);
	}

	public PaymentCancelDto orderCancel(OrderCancelDto orderCancelDto) {
		return paymentService.orderCancel(orderCancelDto);
	}

	public PaymentOrderDetailDto getOrderDetail(String paymentKey) {
		return paymentService.getOrderDetail(paymentKey);
	}
}
