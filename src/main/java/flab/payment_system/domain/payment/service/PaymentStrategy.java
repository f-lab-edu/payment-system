package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;

public interface PaymentStrategy {

	PaymentReadyDto createPayment(PaymentCreateDto paymentCreateDto, Long userId, String requestUrl,
								  Long orderId, Long paymentId, Long productId);

	PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId);

	PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto);

	PaymentOrderDetailDto getOrderDetail(String paymentKey);
}
