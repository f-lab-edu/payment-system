package flab.payment_system.adapter;

import flab.payment_system.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductAdapter {

	private final ProductService productService;

	public void checkRemainStock(Long productId) {
		productService.checkRemainStock(productId);
	}

	public void decreaseStock(Long productId, Integer quantity) {
		productService.decreaseStock(productId, quantity);
	}

	public void increaseStock(Long productId, Integer quantity) {
		productService.increaseStock(productId, quantity);
	}
}
