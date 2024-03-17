package flab.payment_system.adapter;

public interface RedissonLockAdapter {
	void checkRemainStock(Long productId);

	void decreaseStock(Long productId, Integer quantity);

	void increaseStock(Long productId, Integer quantity);
}
