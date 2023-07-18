package flab.payment_system.domain.product.repository;

public interface ProductCustomRepository {

	long updateDecreaseStock(long productId, Integer quantity);

	long updateIncreaseStock(long productId, Integer quantity);
}
