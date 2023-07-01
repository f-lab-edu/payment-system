package flab.payment_system.domain.payment.service;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.response.kakao.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentReadyDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;


@RequestScope
@Component
public class PaymentStrategyTossService implements PaymentStrategy {

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId) {
		return new PaymentKakaoReadyDtoImpl();
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId) {
		return null;
	}


}
