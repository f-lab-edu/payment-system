package flab.payment_system.domain.payment.service;

import flab.payment_system.adapter.PaymentAdapter;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.payment.entity.Payment;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.enums.PaymentStateConstant;
import flab.payment_system.domain.payment.exception.PaymentAlreadyApprovedConflictException;
import flab.payment_system.domain.payment.exception.PaymentNotApprovedConflictException;
import flab.payment_system.domain.payment.exception.PaymentNotExistBadRequestException;
import flab.payment_system.domain.payment.repository.PaymentRepository;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final Map<PaymentPgCompany, PaymentStrategy> paymentStrategies;
	private final PaymentRepository paymentRepository;
	private final PaymentAdapter paymentAdapter;

	private PaymentStrategy getStrategy(PaymentPgCompany paymentPgCompany) {
		return paymentStrategies.get(paymentPgCompany);
	}

	@Transactional
	public PaymentReadyDto createPayment(PaymentCreateDto paymentCreateDto,
		String requestUrl, Long userId, PaymentPgCompany pgCompany) {
		PaymentStrategy paymentStrategy = getStrategy(pgCompany);

		Optional<Payment> optionalPayment = paymentRepository.findByOrderProduct_OrderId(paymentCreateDto.orderId());

		Payment payment = optionalPayment.orElseGet(() -> paymentRepository.save(
			Payment.builder()
				.orderProduct(paymentAdapter.getOrderProductByOrderId(paymentCreateDto.orderId()))
				.state(PaymentStateConstant.ONGOING.getValue())
				.pgCompany(pgCompany.getValue())
				.totalAmount(paymentCreateDto.totalAmount())
				.taxFreeAmount(paymentCreateDto.taxFreeAmount())
				.installMonth(paymentCreateDto.installMonth())
				.build()));

		if (Objects.equals(payment.getState(), PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentAlreadyApprovedConflictException();

		PaymentReadyDto paymentReadyDto = paymentStrategy.createPayment(paymentCreateDto, userId,
			requestUrl, payment.getPaymentId());

		paymentReadyDto.setPaymentId(payment.getPaymentId());

		payment.setPaymentKey(paymentReadyDto.getPaymentKey());

		return paymentReadyDto;
	}

	@Transactional
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId, Long productId,
		Integer quantity, PaymentPgCompany pgCompany) {
		PaymentStrategy paymentStrategy = getStrategy(pgCompany);

		PaymentApprovalDto paymentApprovalDto = paymentStrategy.approvePayment(pgToken, orderId,
			userId, paymentId);

		Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotExistBadRequestException::new);
		if (payment.getState().equals(PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentAlreadyApprovedConflictException();

		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.APPROVED.getValue());

		paymentAdapter.decreaseStock(productId, quantity);

		return paymentApprovalDto;
	}

	@Transactional
	public void failPayment(Long paymentId) {
		Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotExistBadRequestException::new);

		if (payment.getState().equals(PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentAlreadyApprovedConflictException();

		paymentRepository.updatePaymentStateByPaymentId(paymentId,
			PaymentStateConstant.FAIL.getValue());
	}

	@Transactional
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto, PaymentPgCompany pgCompany) {
		PaymentStrategy paymentStrategy = getStrategy(pgCompany);

		Payment payment = paymentRepository.findByOrderProduct_OrderId(orderCancelDto.orderId())
			.orElseThrow(PaymentNotExistBadRequestException::new);
		if (!payment.getState().equals(PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentNotApprovedConflictException();

		PaymentCancelDto paymentCancelDto = paymentStrategy.cancelPayment(orderCancelDto);

		paymentRepository.updatePaymentStateByOrderId(orderCancelDto.orderId(),
			PaymentStateConstant.CANCEL.getValue());

		paymentAdapter.increaseStock(orderCancelDto.productId(), orderCancelDto.quantity());

		return paymentCancelDto;
	}

	public PaymentOrderDetailDto getOrderDetail(String paymentKey, PaymentPgCompany pgCompany) {
		PaymentStrategy paymentStrategy = getStrategy(pgCompany);
		return paymentStrategy.getOrderDetail(paymentKey);
	}

	public Payment getPaymentByPaymentId(Long paymentId) {
		return paymentRepository.findById(paymentId).orElseThrow(PaymentNotExistBadRequestException::new);
	}
}

