package flab.payment_system.domain.payment.client.toss;

import flab.payment_system.core.enums.Constant;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.domain.toss.TossPayment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.exception.PaymentTossServiceUnavailableException;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.repository.toss.TossPaymentRepository;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.client.PaymentStrategy;
import flab.payment_system.domain.payment.response.toss.PaymentTossDtoImpl;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class PaymentStrategyTossClient implements PaymentStrategy {

	private final String tossHost;
	private final String secretKey;
	private final RestTemplate restTemplate;
	private final PaymentRepository paymentRepository;
	private final TossPaymentRepository tossPaymentRepository;

	public PaymentStrategyTossClient(
		@Value("${toss-host}") String tossHost, @Value("${toss-secret-key}") String secretKey
		, RestTemplate restTemplate, PaymentRepository paymentRepository,
		TossPaymentRepository tossPaymentRepository) {
		this.tossHost = tossHost;
		this.secretKey = secretKey;
		this.restTemplate = restTemplate;
		this.paymentRepository = paymentRepository;
		this.tossPaymentRepository = tossPaymentRepository;
	}


	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId, long productId) {
		HttpEntity<Map<String, String>> body = getBodyForCreatePayment(
			orderProductDto, userId, requestUrl, orderId, paymentId, productId);
		return Optional.ofNullable(restTemplate.postForObject(tossHost,
			body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		HttpEntity<Map<String, String>> body = getBodyForApprovePayment(orderId, userId, paymentId);
		PaymentTossDtoImpl paymentTossDto = Optional.ofNullable(
			restTemplate.postForObject(tossHost + "/confirm",
				body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);

		tossPaymentRepository.save(
			TossPayment.builder().paymentId(paymentId).country(paymentTossDto.getCountry())
				.currency(
					paymentTossDto.getCurrency()).type(paymentTossDto.getType()).build());

		return paymentTossDto;
	}

	@Override
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto) {

		HttpEntity<Map<String, String>> body = getBodyForCancelPayment(orderCancelDto);
		return Optional.ofNullable(
			restTemplate.postForObject(tossHost + "/" + orderCancelDto.paymentKey() + "/cancel",
				body, PaymentTossDtoImpl.class)).orElseThrow(
			PaymentTossServiceUnavailableException::new);
	}

	@Override
	public PaymentOrderDetailDto getOrderDetail(String paymentKey) {
		HttpHeaders headers = getHeaders();
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		ResponseEntity<PaymentTossDtoImpl> response = restTemplate.exchange(
			tossHost + "/" + paymentKey, HttpMethod.GET, requestEntity, PaymentTossDtoImpl.class);
		return response.getBody();
	}

	private HttpEntity<Map<String, String>> getBodyForCreatePayment(
		OrderProductDto orderProductDto, long userId, String requestUrl, long orderId,
		long paymentId, long productId) {
		HttpHeaders headers = getHeaders();
		Map<String, String> params = new HashMap<>();

		params.put("apiKey", secretKey);
		params.put("method", "간편결제");
		params.put("taxFreeAmount", String.valueOf(orderProductDto.taxFreeAmount()));
		params.put("orderId", "orderId_" + orderId + "_" + userId);
		params.put("orderName", orderProductDto.productName() + " " + orderProductDto.quantity());
		params.put("amount",
			String.valueOf(orderProductDto.totalAmount()));
		params.put("successUrl",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.TOSS.getName() +
				"/approved?orderId=" + orderId + "&paymentId=" + paymentId + "&pg_token=temp"
				+ "&productId="
				+ productId + "&quantity=" + orderProductDto.quantity());
		params.put("failUrl",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.TOSS.getName() + "/cancel?paymentId=" + paymentId);

		return new HttpEntity<>(params, headers);
	}

	private HttpEntity<Map<String, String>> getBodyForApprovePayment(
		long orderId, long userId, long paymentId) {
		HttpHeaders headers = getHeaders();

		Map<String, String> params = new HashMap<>();

		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(PaymentNotExistBadRequestException::new);

		params.put("paymentKey", payment.getPaymentKey());
		params.put("orderId", "orderId_" + orderId + "_" + userId);
		params.put("amount", String.valueOf(payment.getTotalAmount()));

		return new HttpEntity<>(params, headers);
	}


	private HttpEntity<Map<String, String>> getBodyForCancelPayment(
		OrderCancelDto orderCancelDto) {
		HttpHeaders headers = getHeaders();
		Map<String, String> params = new HashMap<>();

		params.put("cancelReason", "전액취소");
		params.put("cancelAmount", String.valueOf(orderCancelDto.cancelAmount()));
		return new HttpEntity<>(params, headers);
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String authorization = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
		headers.set("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}
}
