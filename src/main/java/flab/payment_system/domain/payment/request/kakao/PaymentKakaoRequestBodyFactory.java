package flab.payment_system.domain.payment.request.kakao;

import flab.payment_system.common.enums.Constant;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.PaymentRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class PaymentKakaoRequestBodyFactory {

	private final String cid;
	private final PaymentRepository paymentRepository;
	private final String adminKey;

	public PaymentKakaoRequestBodyFactory(@Value("${kakao-cid}") String cid,
										  @Value("${kakao-adminkey}") String adminKey,
										  PaymentRepository paymentRepository) {
		this.cid = cid;
		this.adminKey = adminKey;
		this.paymentRepository = paymentRepository;
	}


	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + adminKey);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		return headers;
	}

	public HttpEntity<MultiValueMap<String, String>> getBodyForCreatePayment(
		PaymentCreateDto paymentCreateDto, Long userId, String requestUrl, Long paymentId) {
		HttpHeaders headers = getHeaders();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Optional<Integer> installMonth = paymentCreateDto.getInstallMonth();
		installMonth.ifPresent(integer -> params.add("install_month", String.valueOf(integer)));
		params.add("cid", cid);
		params.add("approval_url",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.KAKAO.getName() +
				"/approved?orderId=" + paymentCreateDto.orderId() + "&paymentId=" + paymentId + "&productId="
				+ paymentCreateDto.productId() + "&quantity=" + paymentCreateDto.quantity());
		params.add("cancel_url",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.KAKAO.getName() + "/cancel?paymentId=" + paymentId);
		params.add("fail_url", requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
			+ PaymentPgCompany.KAKAO.getName()
			+ "/fail?paymentId=" + paymentId);
		params.add("partner_order_id", String.valueOf(paymentCreateDto.orderId()));
		params.add("partner_user_id", String.valueOf(userId));
		params.add("item_name", paymentCreateDto.productName());
		params.add("quantity", String.valueOf(paymentCreateDto.quantity()));
		params.add("total_amount",
			String.valueOf(paymentCreateDto.totalAmount()));
		params.add("tax_free_amount", String.valueOf(paymentCreateDto.taxFreeAmount()));

		return new HttpEntity<>(params, headers);
	}

	public HttpEntity<MultiValueMap<String, String>> getBodyForApprovePayment(String pgToken,
																			  Long orderId, Long userId, Long paymentId) {
		HttpHeaders headers = getHeaders();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		Payment payment = paymentRepository.findById(paymentId)
			.orElseThrow(PaymentNotExistBadRequestException::new);

		params.add("cid", cid);
		params.add("tid", payment.getPaymentKey());
		params.add("partner_order_id", String.valueOf(orderId));
		params.add("partner_user_id", String.valueOf(userId));
		params.add("pg_token", pgToken);
		params.add("total_amount", String.valueOf(payment.getTotalAmount()));
		return new HttpEntity<>(params, headers);
	}

	public HttpEntity<MultiValueMap<String, String>> getBodyForCancelPayment(
		OrderCancelDto orderCancelDto) {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("cid", cid);
		params.add("tid", String.valueOf(orderCancelDto.paymentKey()));
		params.add("cancel_amount", String.valueOf(orderCancelDto.cancelAmount()));
		params.add("cancel_tax_free_amount", String.valueOf(orderCancelDto.cancelTaxFreeAmount()));
		return new HttpEntity<>(params, headers);
	}

	public HttpEntity<MultiValueMap<String, String>> getBodyForOrderDetail(String paymentKey) {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cid", cid);
		params.add("tid", String.valueOf(paymentKey));
		return new HttpEntity<>(params, headers);
	}

}
