package flab.payment_system.domain.payment.client.toss;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.client.PaymentStrategy;
import org.springframework.stereotype.Component;


@Component
public class PaymentStrategyTossClient implements PaymentStrategy {

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId, long productId) {
		return new PaymentKakaoReadyDtoImpl();
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		return null;
	}

	@Override
	public PaymentCancelDto orderCancel(OrderCancelDto orderCancelDto) {
		return null;
	}

	@Override
	public PaymentOrderDetailDto getOrderDetail(String tid) {
		return null;
	}
}
