package flab.payment_system.domain.payment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import flab.payment_system.adapter.PaymentAdapter;
import flab.payment_system.common.response.ResponseMessage;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final PaymentAdapter paymentAdapter;

	@PostMapping("/{pgCompany}")
	public ResponseEntity<PaymentReadyDto> createPayment(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid PaymentCreateDto paymentCreateDto,
		HttpServletRequest request, HttpSession session) {
		String requestUrl = request.getRequestURL().toString()
			.replace(request.getRequestURI(), "");

		Long userId = paymentAdapter.getUserId(session);

		PaymentReadyDto paymentReadyDto = paymentService.createPayment(paymentCreateDto, requestUrl,
			userId, pgCompany);

		return ResponseEntity.ok().body(paymentReadyDto);
	}

	@GetMapping("/{pgCompany}/approved")
	public ResponseEntity<PaymentApprovalDto> paymentApproved(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("pg_token") String pgToken, @RequestParam("orderId") Long orderId,
		@RequestParam("paymentId") Long paymentId, @RequestParam("productId") Long productId
		, @RequestParam("quantity") Integer quantity,
		HttpSession session) {
		Long userId = paymentAdapter.getUserId(session);
		PaymentApprovalDto paymentApprovalDto = paymentService.approvePayment(pgToken, orderId, userId, paymentId,
			productId, quantity, pgCompany);

		return ResponseEntity.ok().body(paymentApprovalDto);
	}

	@GetMapping("/{pgCompany}/fail")
	public ResponseEntity<ResponseMessage> paymentFail(@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("paymentId") Long paymentId) {
		paymentService.failPayment(paymentId);
		return ResponseEntity.ok().body(new ResponseMessage("fail"));
	}

	@GetMapping("/{pgCompany}")
	public ResponseEntity<PaymentOrderDetailDto> paymentOrderDetail(
		@PathVariable PaymentPgCompany pgCompany, @RequestParam String paymentKey) {
		PaymentOrderDetailDto paymentOrderDetailDto = paymentService.getOrderDetail(paymentKey, pgCompany);

		return ResponseEntity.ok().body(paymentOrderDetailDto);
	}
}


