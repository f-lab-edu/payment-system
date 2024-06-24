package flab.payment_system.adapter;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;

public interface OrderAdapter {
	void checkRemainStock(Long productId);

	Long getUserId(HttpSession session);

	PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto, PaymentPgCompany pgCompany);

	Product getProductByProductId(Long productId);

	User getUserByUserId(Long userId);
}
