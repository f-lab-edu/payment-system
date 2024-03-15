package flab.payment_system.domain.redisson.service;

import flab.payment_system.adapter.RedissonLockAdapter;

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
	public void decreaseStock(Long productId, Integer quantity) {
		RLock lock = redissonClient.getLock(String.valueOf(productId));

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				throw new RuntimeException();
			}

			redissonLockAdapter.checkRemainStock(productId);
			redissonLockAdapter.decreaseStock(productId, quantity);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	@Transactional
	public void increaseStock(Long productId, Integer quantity) {
		RLock lock = redissonClient.getLock(String.valueOf(productId));

		try {
			boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);

			if (!available) {
				throw new RuntimeException();
			}

			redissonLockAdapter.checkRemainStock(productId);
			redissonLockAdapter.increaseStock(productId, quantity);

		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}
}
