package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.response.kakao.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.kakao.PaymentReadyDto;

public interface PaymentStrategy {

	PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId, String requestUrl,
		long orderId);

    PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId);
}
