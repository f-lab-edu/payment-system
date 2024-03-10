package flab.payment_system.domain.payment.controller;

import flab.payment_system.adapter.PaymentAdapter;
import flab.payment_system.common.response.ResponseMessage;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import flab.payment_system.domain.payment.service.PaymentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final PaymentAdapter paymentAdapter;

	@GetMapping("/{pgCompany}/approved")
	public ResponseEntity<PaymentApprovalDto> paymentApproved(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestParam("pg_token") String pgToken, @RequestParam("orderId") Long orderId,
		@RequestParam("paymentId") Long paymentId,
		HttpSession session) {
		paymentService.setStrategy(pgCompany);
		Long userId = paymentAdapter.getUserId(session);
		PaymentApprovalDto paymentApprovalDto = paymentAdapter.approvePayment(pgToken, orderId, userId, paymentId);

		return ResponseEntity.ok().body(paymentApprovalDto);
	}

	@GetMapping("/{pgCompany}/fail")
	public ResponseEntity<ResponseMessage> paymentFail(@PathVariable PaymentPgCompany pgCompany,
													   @RequestParam("paymentId") Long paymentId) {
		paymentService.setStrategy(pgCompany);
		paymentService.failPayment(paymentId);
		return ResponseEntity.ok().body(new ResponseMessage("fail"));
	}
}


