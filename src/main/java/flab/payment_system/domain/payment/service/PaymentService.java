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
import flab.payment_system.domain.payment.service.kakao.PaymentStrategyKaKaoService;
import flab.payment_system.domain.payment.service.toss.PaymentStrategyTossService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@RequestScope
public class PaymentService {

	private PaymentStrategy paymentStrategy;
	private final ApplicationContext applicationContext;
	private final PaymentRepository paymentRepository;
	private final PaymentAdapter paymentAdapter;

	public void setStrategy(PaymentPgCompany paymentPgCompany) {
		if (paymentPgCompany == PaymentPgCompany.TOSS) {
			this.paymentStrategy = applicationContext.getBean(PaymentStrategyTossService.class);
		}
		if (paymentPgCompany == PaymentPgCompany.KAKAO) {
			this.paymentStrategy = applicationContext.getBean(PaymentStrategyKaKaoService.class);
		}
	}

	@Transactional
	public PaymentReadyDto createPayment(PaymentCreateDto paymentCreateDto,
										 String requestUrl, Long userId, PaymentPgCompany paymentPgCompany) {
		Optional<Payment> optionalPayment = paymentRepository.findByOrderProduct_OrderId(paymentCreateDto.orderId());

		Payment payment = optionalPayment.orElseGet(() -> paymentRepository.save(
			Payment.builder().orderProduct(paymentAdapter.getOrderProductByOrderId(paymentCreateDto.orderId())).state(PaymentStateConstant.ONGOING.getValue())
				.pgCompany(paymentPgCompany.getValue()).totalAmount(paymentCreateDto.totalAmount())
				.taxFreeAmount(paymentCreateDto.taxFreeAmount())
				.installMonth(paymentCreateDto.installMonth()).build()));

		if (Objects.equals(payment.getState(), PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentAlreadyApprovedConflictException();

		PaymentReadyDto paymentReadyDto = paymentStrategy.createPayment(paymentCreateDto, userId,
			requestUrl, payment.getPaymentId());

		paymentReadyDto.setPaymentId(payment.getPaymentId());

		payment.setPaymentKey(paymentReadyDto.getPaymentKey());

		return paymentReadyDto;
	}

	@Transactional
	public PaymentApprovalDto approvePayment(String pgToken, Long orderId, Long userId, Long paymentId, Long productId, Integer quantity) {

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
	public PaymentCancelDto cancelPayment(OrderCancelDto orderCancelDto) {
		Payment payment = paymentRepository.findByOrderProduct_OrderId(orderCancelDto.orderId()).orElseThrow(PaymentNotExistBadRequestException::new);
		if (!payment.getState().equals(PaymentStateConstant.APPROVED.getValue()))
			throw new PaymentNotApprovedConflictException();

		PaymentCancelDto paymentCancelDto = paymentStrategy.cancelPayment(orderCancelDto);

		paymentRepository.updatePaymentStateByOrderId(orderCancelDto.orderId(),
			PaymentStateConstant.CANCEL.getValue());

		paymentAdapter.increaseStock(orderCancelDto.productId(), orderCancelDto.quantity());

		return paymentCancelDto;
	}

	public PaymentOrderDetailDto getOrderDetail(String paymentKey) {
		return paymentStrategy.getOrderDetail(paymentKey);
	}

	public Payment getPaymentByPaymentId(Long paymentId) {
		return paymentRepository.findById(paymentId).orElseThrow(PaymentNotExistBadRequestException::new);
	}
}

