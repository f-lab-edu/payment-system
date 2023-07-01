package flab.payment_system.domain.order.service;

import flab.payment_system.domain.order.domain.Order;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.repository.OrderRepository;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.exception.PaymentFailBadRequestException;
import flab.payment_system.domain.product.service.ProductService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrderService {

	private final ProductService productService;
	private final OrderRepository orderRepository;

	public long orderProduct(OrderProductDto orderProductDto, long userId,
		PaymentPgCompany paymentPgCompany) {
		productService.checkRemainStock(orderProductDto);
		AtomicReference<Integer> installMonth = new AtomicReference<>(0);
		Optional<Integer> optionalInstallMonth = orderProductDto.getInstallMonth();
		optionalInstallMonth.ifPresent(installMonth::set);

		Order order = orderRepository.save(
			Order.builder().productId(orderProductDto.productId()).userId(userId)
				.pgCompany(paymentPgCompany.getValue())
				.quantity(orderProductDto.quantity()).totalAmount(orderProductDto.totalAmount())
				.taxFreeAmount(orderProductDto.taxFreeAmount())
				.installMonth(installMonth.get()).build());
		return order.getOrderId();
	}

	public long getOrderId(HttpServletRequest request) {
		Cookie[] cookies = Optional.ofNullable(request.getCookies())
			.orElseThrow(PaymentFailBadRequestException::new);

		Optional<Cookie> optionalOrderId = Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals("orderId"))
			.findFirst();

		return Long.parseLong(optionalOrderId.orElseThrow(PaymentFailBadRequestException::new)
			.getValue());
	}
}

