package flab.payment_system.adapter;

import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedissonLockAdapterImpl implements RedissonLockAdapter {
	private final ProductService productService;
	private final PaymentService paymentService;

	@Override
	public void checkRemainStock(Long productId) {
		productService.checkRemainStock(productId);
	}

	@Override
	public void decreaseStock(Long productId, Integer quantity) {
		productService.decreaseStock(productId, quantity);
	}

	@Override
	public void increaseStock(Long productId, Integer quantity) {
		productService.increaseStock(productId, quantity);
	}
}
