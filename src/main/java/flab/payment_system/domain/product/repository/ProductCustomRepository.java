package flab.payment_system.domain.product.repository;

import flab.payment_system.domain.product.entity.Product;

import java.util.List;

public interface ProductCustomRepository {
	List<Product> findByCursor(Long lastProductId, long size);
}
