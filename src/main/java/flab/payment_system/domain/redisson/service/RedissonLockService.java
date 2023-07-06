package flab.payment_system.domain.redisson.service;

import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.product.service.ProductService;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RedissonLockService {

	private final RedissonClient redissonClient;
	private final ProductService productService;
	private final PaymentService paymentService;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId,
		Long paymentId,
		Long productId, Integer quantity) {
		RLock lock = redissonClient.getLock(String.valueOf(productId));

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				throw new RuntimeException();
			}
			productService.checkRemainStock(productId);
			PaymentApprovalDto paymentApprovalDto = paymentService.approvePayment(pgToken, orderId,
				userId, paymentId);

			productService.decreaseStock(productId, quantity);

			return paymentApprovalDto;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
