package flab.payment_system.domain.order.controller;


import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

	private final OrderService orderService;
	private final PaymentService paymentService;
	private final UserService userService;

	@PostMapping("/{pgCompany}")
	public ResponseEntity<PaymentReadyDto> orderProductRequest(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody OrderProductDto orderProductDto,
		HttpServletRequest request, HttpSession session) {

		long userId = userService.getUserIdByRequest(request, session);
		long orderId = orderService.orderProduct(orderProductDto, userId);
		paymentService.setStrategy(pgCompany);
		PaymentReadyDto paymentReadyDto = paymentService.createPayment(orderProductDto, request,
			userId,
			orderId, pgCompany);

		return ResponseEntity.ok().body(paymentReadyDto);
	}
}

