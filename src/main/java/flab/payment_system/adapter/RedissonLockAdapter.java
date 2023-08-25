package flab.payment_system.adapter;

import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.redisson.service.RedissonLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedissonLockAdapter {

	private final RedissonLockService redissonLockService;

	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId,
		Long paymentId,
		Long productId, Integer quantity) {

		return redissonLockService.approvePayment(pgToken, orderId, userId, paymentId, productId,
			quantity);
	}
}
