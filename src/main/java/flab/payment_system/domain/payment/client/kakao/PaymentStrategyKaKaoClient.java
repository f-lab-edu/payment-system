package flab.payment_system.domain.payment.client.kakao;

import flab.payment_system.core.enums.Constant;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.domain.kakao.KakaoPayment;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentKaKaoServiceUnavailableException;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.kakao.KakaoPaymentRepository;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoApprovalDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoCancelDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoOrderDetailDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.client.PaymentStrategy;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Component
public class PaymentStrategyKaKaoClient implements PaymentStrategy {

	private final String cid;
	private final String kakaoHost;
	private final String adminKey;
	private final RestTemplate restTemplate;
	private final PaymentRepository paymentRepository;

	private final KakaoPaymentRepository kakaoPaymentRepository;

	public PaymentStrategyKaKaoClient(@Value("${kakao-cid}") String cid,
		@Value("${kakao-host}") String kakaoHost, @Value("${kakao-adminkey}") String adminKey,
		RestTemplate restTemplate,
		PaymentRepository paymentRepository, KakaoPaymentRepository kakaoPaymentRepository) {
		this.cid = cid;
		this.kakaoHost = kakaoHost;
		this.adminKey = adminKey;
		this.restTemplate = restTemplate;
		this.paymentRepository = paymentRepository;
		this.kakaoPaymentRepository = kakaoPaymentRepository;
	}

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId, long productId) {

		HttpEntity<MultiValueMap<String, String>> body = getBodyForCreatePayment(
			orderProductDto, userId, requestUrl, orderId, paymentId, productId);

		return Optional.ofNullable(
			restTemplate.postForObject(kakaoHost + "/ready",
				body, PaymentKakaoReadyDtoImpl.class)).orElseThrow(
			PaymentKaKaoServiceUnavailableException::new);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		HttpEntity<MultiValueMap<String, String>> body = getBodyForApprovePayment(pgToken, orderId,
			userId, paymentId);

		PaymentKakaoApprovalDtoImpl paymentApprovalDto = Optional.ofNullable(
				restTemplate.postForObject(kakaoHost + "/approve",
					body,
					PaymentKakaoApprovalDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);

		kakaoPaymentRepository.save(KakaoPayment.builder().paymentId(paymentId)
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).aid(
				paymentApprovalDto.getAid())
			.cardInfo(paymentApprovalDto.getCardInfo())
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).build());

		return paymentApprovalDto;
	}

	@Override
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto) {
		HttpEntity<MultiValueMap<String, String>> body = getBodyForCancelPayment(orderCancelDto);

		PaymentCancelDto paymentCancelDto = Optional.ofNullable(
				restTemplate.postForObject(kakaoHost + "/cancel",
					body,
					PaymentKakaoCancelDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);

		return paymentCancelDto;
	}

	@Override
	public PaymentOrderDetailDto getOrderDetail(String tid) {
		HttpEntity<MultiValueMap<String, String>> body = getBodyForOrderDetail(tid);

		return Optional.ofNullable(
				restTemplate.postForObject(kakaoHost + "/order",
					body,
					PaymentKakaoOrderDetailDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);
	}


	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + adminKey);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		return headers;
	}

	private HttpEntity<MultiValueMap<String, String>> getBodyForCreatePayment(
		OrderProductDto orderProductDto,
		long userId, String requestUrl, long orderId, long paymentId, long productId) {
		HttpHeaders headers = getHeaders();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		Optional<Integer> installMonth = orderProductDto.getInstallMonth();
		installMonth.ifPresent(integer -> params.add("install_month", String.valueOf(integer)));
		params.add("cid", cid);
		params.add("approval_url",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.KAKAO.getName() +
				"/approved?orderId=" + orderId + "&paymentId=" + paymentId);
		params.add("cancel_url",
			requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
				+ PaymentPgCompany.KAKAO.getName() + "/cancel?paymentId=" + paymentId + "&productId"
				+ productId);
		params.add("fail_url", requestUrl + Constant.API_AND_VERSION.getValue() + "/payment/"
			+ PaymentPgCompany.KAKAO.getName()
			+ "/fail?paymentId=" + paymentId + "&productId" + productId);
		params.add("partner_order_id", String.valueOf(orderId));
		params.add("partner_user_id", String.valueOf(userId));
		params.add("item_name", orderProductDto.productName());
		params.add("quantity", String.valueOf(orderProductDto.quantity()));
		params.add("total_amount",
			String.valueOf(orderProductDto.totalAmount()));
		params.add("tax_free_amount", String.valueOf(orderProductDto.taxFreeAmount()));

		return new HttpEntity<>(params, headers);
	}

	private HttpEntity<MultiValueMap<String, String>> getBodyForApprovePayment(String pgToken,
		long orderId, long userId, long paymentId) {
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

	private HttpEntity<MultiValueMap<String, String>> getBodyForCancelPayment(
		OrderCancelDto orderCancelDto) {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		params.add("cid", cid);
		params.add("tid", String.valueOf(orderCancelDto.paymentKey()));
		params.add("cancel_amount", String.valueOf(orderCancelDto.cancelAmount()));
		params.add("cancel_tax_free_amount", String.valueOf(orderCancelDto.cancelTaxFreeAmount()));
		return new HttpEntity<>(params, headers);
	}

	private HttpEntity<MultiValueMap<String, String>> getBodyForOrderDetail(String paymentKey) {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("cid", cid);
		params.add("tid", String.valueOf(paymentKey));
		return new HttpEntity<>(params, headers);
	}

}