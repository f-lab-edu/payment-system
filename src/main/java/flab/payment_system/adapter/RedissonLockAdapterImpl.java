package flab.payment_system.adapter;

import flab.payment_system.domain.payment.response.PaymentApprovalDto;
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
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId) {
		return paymentService.approvePayment(pgToken, orderId, userId, paymentId);
	}

	@Override
	public void decreaseStock(Long productId, Integer quantity) {
		productService.decreaseStock(productId, quantity);
	}
}
