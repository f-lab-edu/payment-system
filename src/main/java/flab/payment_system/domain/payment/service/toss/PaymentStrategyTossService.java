package flab.payment_system.domain.payment.service.toss;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import org.springframework.stereotype.Component;


@Component
public class PaymentStrategyTossService implements PaymentStrategy {

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId) {
		return new PaymentKakaoReadyDtoImpl();
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		return null;
	}
}
