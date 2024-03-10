package flab.payment_system.domain.order.controller;


import flab.payment_system.adapter.OrderAdapter;
import flab.payment_system.domain.order.dto.OrderCancelDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.service.OrderService;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
	private final OrderAdapter orderAdapter;

	@Transactional
	@PostMapping("/{pgCompany}")
	public ResponseEntity<PaymentReadyDto> orderProductRequest(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid OrderProductDto orderProductDto,
		HttpServletRequest request, HttpSession session) {
		String requestUrl = request.getRequestURL().toString()
			.replace(request.getRequestURI(), "");

		Long userId = orderAdapter.getUserId(session);

		orderAdapter.checkRemainStock(orderProductDto.productId());

		Long orderId = orderService.orderProduct(orderProductDto, userId);

		orderAdapter.setStrategy(pgCompany);
		PaymentReadyDto paymentReadyDto = orderAdapter.createPayment(orderProductDto, requestUrl,
			userId,
			orderId, pgCompany);

		return ResponseEntity.ok().body(paymentReadyDto);
	}


	@Transactional
	@PostMapping("/{pgCompany}/cancel")
	public ResponseEntity<PaymentCancelDto> orderCancel(
		@PathVariable PaymentPgCompany pgCompany,
		@RequestBody @Valid OrderCancelDto orderCancelDto) {
		orderAdapter.setStrategy(pgCompany);
		PaymentCancelDto paymentCancelDto = orderAdapter.orderCancel(orderCancelDto);
		orderAdapter.increaseStock(orderCancelDto.productId(), orderCancelDto.quantity());

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

