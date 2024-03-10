package flab.payment_system.adapter;

import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentAdapterImpl implements PaymentAdapter {

	private final UserService userService;
	private final PaymentService paymentService;
	private final OrderService orderService;

	@Override
	public Long getUserId(HttpSession session) {
		return userService.getUserId(session);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId) {
		return paymentService.approvePayment(pgToken, orderId, userId, paymentId);
	}

	@Override
	public OrderProduct getOrderProductByOrderId(Long orderId) {
		return orderService.getOrderProductByOrderId(orderId);
	}

	@Override
	public Payment getPaymentByPaymentId(Long paymentId) {
		return paymentService.getPaymentByPaymentId(paymentId);
	}

}
