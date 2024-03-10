package flab.payment_system.adapter;

import flab.payment_system.domain.payment.response.PaymentApprovalDto;

public interface RedissonLockAdapter {
	void checkRemainStock(Long productId);

	PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId);

	void decreaseStock(Long productId, Integer quantity);
}
