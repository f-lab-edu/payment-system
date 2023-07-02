package flab.payment_system.domain.payment.controller;

import flab.payment_system.core.response.ResponseMessage;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.session.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final SessionService sessionService;
	private final ProductService productService;

	@GetMapping("/{pgCompany}/approved")
	public ResponseEntity<PaymentApprovalDto> paymentApproved(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("pg_token") String pgToken, @RequestParam("orderId") Long orderId,
		@RequestParam("paymentId") Long paymentId, HttpServletRequest request,
		HttpSession session) {
		paymentService.setStrategy(pgCompany);
		long userId = sessionService.getUserIdByRequest(request, session);

		PaymentApprovalDto paymentApprovalDto = paymentService.approvePayment(pgToken, orderId,
			userId, paymentId);

		return ResponseEntity.ok().body(paymentApprovalDto);
	}

	@GetMapping("/{pgCompany}/cancel")
	public ResponseEntity<ResponseMessage> paymentCancel(@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("paymentId") Long paymentId, @RequestParam("productId") Long productId) {
		paymentService.setStrategy(pgCompany);
		paymentService.cancelPayment(paymentId);
		productService.increaseStock(productId);
		return ResponseEntity.ok().body(new ResponseMessage("cancel"));
	}

	@GetMapping("/{pgCompany}/fail")
	public ResponseEntity<ResponseMessage> paymentFail(@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("paymentId") Long paymentId, @RequestParam("productId") Long productId) {
		paymentService.setStrategy(pgCompany);
		paymentService.failPayment(paymentId);
		productService.increaseStock(productId);
		return ResponseEntity.ok().body(new ResponseMessage("fail"));
	}
}


