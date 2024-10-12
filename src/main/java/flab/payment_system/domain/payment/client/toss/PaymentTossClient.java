package flab.payment_system.domain.payment.client.toss;

import flab.payment_system.domain.payment.exception.PaymentTossServiceUnavailableException;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.response.toss.PaymentTossDtoImpl;

import java.util.Map;
import java.util.Optional;

import flab.payment_system.domain.payment.response.toss.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class PaymentTossClient {

	private final RestTemplate restTemplate;


	public PaymentReadyDto createPayment(String url, HttpEntity<Map<String, String>> body) {
		return Optional.ofNullable(restTemplate.postForObject(url,
			body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);
	}

	public PaymentTossDtoImpl approvePayment(String url, HttpEntity<Map<String, String>> body) {
		return Optional.ofNullable(
			restTemplate.postForObject(url,
				body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);
	}

	public PaymentCancelDto cancelPayment(String url, HttpEntity<Map<String, String>> body) {
		return Optional.ofNullable(
			restTemplate.postForObject(url,
				body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);
	}

	public PaymentOrderDetailDto getOrderDetail(String url, HttpEntity<Void> requestEntity) {
		ResponseEntity<PaymentTossDtoImpl> response = restTemplate.exchange(
			url, HttpMethod.GET, requestEntity, PaymentTossDtoImpl.class);
		return response.getBody();
	}

	public Settlement[] getSettlementList(String url, HttpEntity<Void> requestEntity) {
		ResponseEntity<Settlement[]> response = restTemplate.exchange(
			url, HttpMethod.GET, requestEntity, Settlement[].class);
		return response.getBody();
	}
}
