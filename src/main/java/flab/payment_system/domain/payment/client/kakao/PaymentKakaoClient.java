package flab.payment_system.domain.payment.client.kakao;

import flab.payment_system.domain.payment.exception.PaymentKaKaoServiceUnavailableException;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoApprovalDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoCancelDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoOrderDetailDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class PaymentKakaoClient {

	private final RestTemplate restTemplate;


	public PaymentReadyDto createPayment(String url,
		HttpEntity<MultiValueMap<String, String>> body) {

		return Optional.ofNullable(
			restTemplate.postForObject(url,
				body, PaymentKakaoReadyDtoImpl.class)).orElseThrow(
			PaymentKaKaoServiceUnavailableException::new);
	}


	public PaymentKakaoApprovalDtoImpl approvePayment(String url,
		HttpEntity<MultiValueMap<String, String>> body) {

		return Optional.ofNullable(
				restTemplate.postForObject(url,
					body,
					PaymentKakaoApprovalDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);
	}

	public PaymentCancelDto cancelPayment(String url,
		HttpEntity<MultiValueMap<String, String>> body) {
		return Optional.ofNullable(
				restTemplate.postForObject(url,
					body,
					PaymentKakaoCancelDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);
	}

	public PaymentOrderDetailDto getOrderDetail(String url,
		HttpEntity<MultiValueMap<String, String>> body) {

		return Optional.ofNullable(
				restTemplate.postForObject(url,
					body,
					PaymentKakaoOrderDetailDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);
	}


}
