package flab.payment_system.domain.order.controller;

import flab.payment_system.adapter.OrderAdapter;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
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
	private final OrderAdapter orderAdapter;

	@PostMapping
	public ResponseEntity<OrderDto> orderProductRequest(
		@RequestBody @Valid OrderProductDto orderProductDto, HttpSession session) {
		Long userId = orderAdapter.getUserId(session);
		OrderDto orderDto = orderService.orderProduct(orderProductDto, userId);

		return ResponseEntity.ok().body(orderDto);
	}

	@PostMapping("/{pgCompany}/cancel")
	public ResponseEntity<PaymentCancelDto> orderCancel(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid OrderCancelDto orderCancelDto) {
		orderAdapter.setStrategy(pgCompany);
		PaymentCancelDto paymentCancelDto = orderAdapter.cancelPayment(orderCancelDto);

		return ResponseEntity.ok().body(paymentCancelDto);
	}

	@GetMapping("/{pgCompany}")
	public ResponseEntity<PaymentOrderDetailDto> getOrderDetail(
		@PathVariable PaymentPgCompany pgCompany, @RequestParam String paymentKey) {
		orderAdapter.setStrategy(pgCompany);
		PaymentOrderDetailDto paymentOrderDetailDto = orderAdapter.getOrderDetail(paymentKey);

		return ResponseEntity.ok().body(paymentOrderDetailDto);
	}
}

