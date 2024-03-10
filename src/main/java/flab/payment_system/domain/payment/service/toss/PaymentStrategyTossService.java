package flab.payment_system.domain.payment.service.toss;


import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.payment.client.toss.PaymentTossClient;
import flab.payment_system.domain.payment.domain.toss.TossPayment;
import flab.payment_system.domain.payment.repository.toss.TossPaymentRepository;
import flab.payment_system.domain.payment.request.toss.PaymentTossRequestBodyFactory;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.response.toss.PaymentTossDtoImpl;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;


@Component
public class PaymentStrategyTossService implements PaymentStrategy {

	private final String tossHost;
	private final TossPaymentRepository tossPaymentRepository;
	private final PaymentTossRequestBodyFactory paymentTossRequestBodyFactory;
	private final PaymentTossClient paymentTossClient;

	public PaymentStrategyTossService(
		@Value("${toss-host}") String tossHost,
		TossPaymentRepository tossPaymentRepository,
		PaymentTossRequestBodyFactory paymentTossRequestBodyFactory,
		PaymentTossClient paymentTossClient) {
		this.tossHost = tossHost;
		this.tossPaymentRepository = tossPaymentRepository;
		this.paymentTossRequestBodyFactory = paymentTossRequestBodyFactory;
		this.paymentTossClient = paymentTossClient;
	}


	@Override
	public PaymentReadyDto createPayment(OrderProductDto orderProductDto, Long userId,
		String requestUrl, Long orderId, Long paymentId, Long productId) {
		HttpEntity<Map<String, String>> body = paymentTossRequestBodyFactory.getBodyForCreatePayment(
			orderProductDto, userId, requestUrl, orderId, paymentId, productId);
		return paymentTossClient.createPayment(tossHost, body);
	}

	@Override
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId,
		Long paymentId) {
		HttpEntity<Map<String, String>> body = paymentTossRequestBodyFactory.getBodyForApprovePayment(
			orderId, userId, paymentId);
		PaymentTossDtoImpl paymentTossDto = paymentTossClient.approvePayment(tossHost + "/confirm",
			body);

		tossPaymentRepository.save(
			TossPayment.builder().paymentId(paymentId).country(paymentTossDto.getCountry())
				.currency(
					paymentTossDto.getCurrency()).type(paymentTossDto.getType()).build());

		return paymentTossDto;
	}

	@Override
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto) {

		HttpEntity<Map<String, String>> body = paymentTossRequestBodyFactory.getBodyForCancelPayment(
			orderCancelDto);
		return paymentTossClient.cancelPayment(
			tossHost + "/" + orderCancelDto.paymentKey() + "/cancel", body);
	}

	@Override
	public PaymentOrderDetailDto getOrderDetail(String paymentKey) {
		HttpHeaders headers = paymentTossRequestBodyFactory.getHeaders();
		HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
		return paymentTossClient.getOrderDetail(tossHost + "/" + paymentKey, requestEntity);
	}
}
