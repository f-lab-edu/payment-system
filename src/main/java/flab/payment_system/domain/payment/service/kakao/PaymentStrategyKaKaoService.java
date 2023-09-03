package flab.payment_system.domain.payment.service.kakao;

import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.client.kakao.PaymentKakaoClient;
import flab.payment_system.domain.payment.domain.kakao.KakaoPayment;
import flab.payment_system.domain.payment.repository.kakao.KakaoPaymentRepository;
import flab.payment_system.domain.payment.request.kakao.PaymentKakaoRequestBodyFactory;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoApprovalDtoImpl;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;


@Component
public class PaymentStrategyKaKaoService implements PaymentStrategy {

	private final String kakaoHost;
	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final PaymentKakaoRequestBodyFactory paymentKakaoRequestBodyFactory;
	private final PaymentKakaoClient paymentKakaoClient;

	public PaymentStrategyKaKaoService(
		@Value("${kakao-host}") String kakaoHost,
		KakaoPaymentRepository kakaoPaymentRepository,
		PaymentKakaoRequestBodyFactory paymentKakaoRequestBodyFactory,
		PaymentKakaoClient paymentKakaoClient) {
		this.kakaoHost = kakaoHost;
		this.kakaoPaymentRepository = kakaoPaymentRepository;
		this.paymentKakaoRequestBodyFactory = paymentKakaoRequestBodyFactory;
		this.paymentKakaoClient = paymentKakaoClient;
	}

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId, long productId) {

		HttpEntity<MultiValueMap<String, String>> body = paymentKakaoRequestBodyFactory.getBodyForCreatePayment(
			orderProductDto, userId, requestUrl, orderId, paymentId, productId);

		return paymentKakaoClient.createPayment(kakaoHost + "/ready", body);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		HttpEntity<MultiValueMap<String, String>> body = paymentKakaoRequestBodyFactory.getBodyForApprovePayment(
			pgToken, orderId,
			userId, paymentId);

		PaymentKakaoApprovalDtoImpl paymentApprovalDto = paymentKakaoClient.approvePayment(
			kakaoHost + "/approve", body);

		kakaoPaymentRepository.save(KakaoPayment.builder().paymentId(paymentId)
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).aid(
				paymentApprovalDto.getAid())
			.cardInfo(paymentApprovalDto.getCardInfo())
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).build());

		return paymentApprovalDto;
	}

	@Override
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto) {
		HttpEntity<MultiValueMap<String, String>> body = paymentKakaoRequestBodyFactory.getBodyForCancelPayment(
			orderCancelDto);

		return paymentKakaoClient.cancelPayment(kakaoHost + "/cancel",
			body);
	}

	@Override
	public PaymentOrderDetailDto getOrderDetail(String tid) {
		HttpEntity<MultiValueMap<String, String>> body = paymentKakaoRequestBodyFactory.getBodyForOrderDetail(
			tid);

		return paymentKakaoClient.getOrderDetail(kakaoHost + "/order", body);
	}
}
