package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;

public interface PaymentStrategy {

	PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId, String requestUrl,
		long orderId, long paymentId);

    PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId, long paymentId);
}