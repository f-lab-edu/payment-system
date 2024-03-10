package flab.payment_system.adapter;

import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import jakarta.servlet.http.HttpSession;

public interface PaymentAdapter {
	Long getUserId(HttpSession session);

	PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId);
}
