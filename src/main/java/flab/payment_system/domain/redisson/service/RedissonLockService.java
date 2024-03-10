package flab.payment_system.domain.redisson.service;

import flab.payment_system.adapter.RedissonLockAdapter;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedissonLockService {

	private final RedissonClient redissonClient;
	private final RedissonLockAdapter redissonLockAdapter;

	@Transactional
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId,
		Long paymentId,
		Long productId, Integer quantity) {
		RLock lock = redissonClient.getLock(String.valueOf(productId));

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				throw new RuntimeException();
			}
			redissonLockAdapter.checkRemainStock(productId);
			PaymentApprovalDto paymentApprovalDto = redissonLockAdapter.approvePayment(pgToken, orderId,
				userId, paymentId);

			redissonLockAdapter.decreaseStock(productId, quantity);

			return paymentApprovalDto;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
