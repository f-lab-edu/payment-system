package flab.payment_system.domain.payment.service.kakao;

import flab.payment_system.core.enums.Constant;
import flab.payment_system.domain.order.domain.Order;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.exception.OrderNotExistBadRequestException;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.payment.domain.kakao.KakaoPayment;
import flab.payment_system.domain.payment.domain.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.exception.PaymentKaKaoServiceUnavailableException;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.KakaoPaymentRepository;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.kakao.PaymentKaKaoApprovalDtoImpl;
import flab.payment_system.domain.payment.response.kakao.PaymentKakaoReadyDtoImpl;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentStrategy;
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
public class PaymentStrategyKaKaoService implements PaymentStrategy {

	private final String cid;
	private final String kakaoHost;
	private final String adminKey;
	private final String host;
	private final RestTemplate restTemplate;
	private final PaymentRepository paymentRepository;

	private final KakaoPaymentRepository kakaoPaymentRepository;
	private final OrderRepository orderRepository;

	public PaymentStrategyKaKaoService(@Value("${kakao-cid}") String cid,
		@Value("${kakao-host}") String kakaoHost, @Value("${kakao-adminkey}") String adminKey,
		@Value("${host}") String host, RestTemplate restTemplate,
		PaymentRepository paymentRepository, KakaoPaymentRepository kakaoPaymentRepository,
		OrderRepository orderRepository) {
		this.cid = cid;
		this.kakaoHost = kakaoHost;
		this.adminKey = adminKey;
		this.host = host;
		this.restTemplate = restTemplate;
		this.paymentRepository = paymentRepository;
		this.orderRepository = orderRepository;
		this.kakaoPaymentRepository = kakaoPaymentRepository;
	}

	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, long userId,
		String requestUrl, long orderId, long paymentId, long productId) {
		HttpHeaders headers = getHeaders();

		MultiValueMap<String, String> params = getParamsForCreatePayment(
			orderProductDto, userId, requestUrl, orderId, paymentId, productId);

		HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

		return Optional.ofNullable(restTemplate.postForObject(kakaoHost + "/ready",
			body, PaymentKakaoReadyDtoImpl.class)).orElseThrow(
			PaymentKaKaoServiceUnavailableException::new);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, long orderId, long userId,
		long paymentId) {
		HttpHeaders headers = getHeaders();
		MultiValueMap<String, String> params = getParamsForApprovePayment(pgToken, orderId, userId);
		HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<>(params, headers);

		PaymentApprovalDto paymentApprovalDto = Optional.ofNullable(
				restTemplate.postForObject(kakaoHost + "/approve",
					body,
					PaymentKaKaoApprovalDtoImpl.class))
			.orElseThrow(PaymentKaKaoServiceUnavailableException::new);

		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.APPROVED.getValue());

		kakaoPaymentRepository.save(KakaoPayment.builder().paymentId(paymentId)
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).aid(
				paymentApprovalDto.getAid()).amount(paymentApprovalDto.getAmount())
			.cardInfo(paymentApprovalDto.getCardInfo())
			.paymentMethodType(paymentApprovalDto.getPaymentMethodType()).build());

		return paymentApprovalDto;
	}


	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "KakaoAK " + adminKey);
		headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
		return headers;
	}

	private MultiValueMap<String, String> getParamsForCreatePayment(OrderProductDto orderProductDto,
		long userId, String requestUrl, long orderId, long paymentId, long productId) {
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
		return params;
	}

	private MultiValueMap<String, String> getParamsForApprovePayment(String pgToken,
		long orderId, long userId) {

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

		Payment payment = paymentRepository.findByOrderId(orderId)
			.orElseThrow(PaymentNotExistBadRequestException::new);

		params.add("cid", cid);
		params.add("tid", payment.getTid());
		params.add("partner_order_id", String.valueOf(orderId));
		params.add("partner_user_id", String.valueOf(userId));
		params.add("pg_token", pgToken);
		params.add("total_amount", String.valueOf(payment.getTotalAmount()));
		return params;
	}

}
