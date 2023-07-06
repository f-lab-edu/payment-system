package flab.payment_system.domain.order.controller;


import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.product.service.ProductService;
import flab.payment_system.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order")
public class OrderController {

	private final OrderService orderService;
	private final PaymentService paymentService;
	private final UserService userService;
	private final ProductService productService;

	@PostMapping("/{pgCompany}")
	public ResponseEntity<PaymentReadyDto> orderProductRequest(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid OrderProductDto orderProductDto,
		HttpServletRequest request, HttpSession session) {
		productService.checkRemainStock(orderProductDto.productId());

		long userId = userService.getUserId(session);

		long orderId = orderService.orderProduct(orderProductDto, userId);

		paymentService.setStrategy(pgCompany);
		PaymentReadyDto paymentReadyDto = paymentService.createPayment(orderProductDto, request,
			userId,
			orderId, pgCompany);

		return ResponseEntity.ok().body(paymentReadyDto);
	}

	@PostMapping("/{pgCompany}/cancel")
	public ResponseEntity<PaymentCancelDto> orderCancel(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid OrderCancelDto orderCancelDto) {
		paymentService.setStrategy(pgCompany);
		PaymentCancelDto paymentCancelDto = paymentService.orderCancel(orderCancelDto);

		return ResponseEntity.ok().body(paymentCancelDto);
	}

	@GetMapping("/{pgCompany}")
	public ResponseEntity<PaymentOrderDetailDto> getOrderDetail(
		@PathVariable PaymentPgCompany pgCompany, @RequestParam String paymentKey) {
		paymentService.setStrategy(pgCompany);
		PaymentOrderDetailDto paymentOrderDetailDto = paymentService.getOrderDetail(paymentKey);
		return ResponseEntity.ok().body(paymentOrderDetailDto);
	}
}

