package flab.payment_system.domain.payment.request.toss;

import flab.payment_system.core.enums.Constant;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class PaymentTossRequestBodyFactory {


	private final String secretKey;
	private final PaymentRepository paymentRepository;

	public PaymentTossRequestBodyFactory(@Value("${toss-secret-key}") String secretKey
		, PaymentRepository paymentRepository) {
		this.secretKey = secretKey;
		this.paymentRepository = paymentRepository;
	}


	public HttpEntity<Map<String, String>> getBodyForCreatePayment(
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

	public HttpEntity<Map<String, String>> getBodyForApprovePayment(
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


	public HttpEntity<Map<String, String>> getBodyForCancelPayment(
		OrderCancelDto orderCancelDto) {
		HttpHeaders headers = getHeaders();
		Map<String, String> params = new HashMap<>();

		params.put("cancelReason", "전액취소");
		params.put("cancelAmount", String.valueOf(orderCancelDto.cancelAmount()));
		return new HttpEntity<>(params, headers);
	}

	public HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		String authorization = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
		headers.set("Authorization", "Basic " + authorization);
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

}
