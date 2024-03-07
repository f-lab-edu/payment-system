package flab.payment_system.domain.product.repository;

public interface ProductCustomRepository {
	List<Product> findByCursor(Long lastProductId, long size);
}
