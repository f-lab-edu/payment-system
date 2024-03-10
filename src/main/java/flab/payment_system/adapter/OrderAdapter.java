package flab.payment_system.adapter;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.user.entity.User;
import jakarta.servlet.http.HttpSession;

public interface OrderAdapter {
	void checkRemainStock(Long productId);

	void increaseStock(Long productId, Integer quantity);

	Long getUserId(HttpSession session);


	void setStrategy(PaymentPgCompany pgCompany);

	PaymentReadyDto createPayment(OrderProductDto orderProductDto, String requestUrl,
										 Long userId, Long orderId, PaymentPgCompany pgCompany);

	PaymentCancelDto orderCancel(OrderCancelDto orderCancelDto);

	PaymentOrderDetailDto getOrderDetail(String paymentKey);

	Product getProductByProductId(Long productId);

	User getUserByUserId(Long userId);
}
