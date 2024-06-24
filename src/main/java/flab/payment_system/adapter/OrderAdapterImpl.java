package flab.payment_system.adapter;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.service.ProductService;
import flab.payment_system.domain.user.entity.User;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderAdapterImpl implements OrderAdapter {
	private final UserService userService;
	private final ProductService productService;
	private final PaymentService paymentService;

	@Override
	public void checkRemainStock(Long productId) {
		productService.checkRemainStock(productId);
	}

	@Override
	public Long getUserId(HttpSession session) {
		return userService.getUserId(session);
	}

	@Override
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto, PaymentPgCompany pgCompany) {
		return paymentService.cancelPayment(orderCancelDto, pgCompany);
	}

	@Override
	public Product getProductByProductId(Long productId) {
		return productService.getProductByProductId(productId);
	}

	@Override
	public User getUserByUserId(Long userId) {
		return userService.getUserByUserId(userId);
	}
}
