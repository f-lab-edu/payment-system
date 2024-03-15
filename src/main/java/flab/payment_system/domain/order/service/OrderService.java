package flab.payment_system.domain.order.service;

import flab.payment_system.adapter.OrderAdapter;
import flab.payment_system.domain.order.dto.OrderDto;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.payment.dto.PaymentCreateDto;
import flab.payment_system.domain.order.exception.OrderNotExistBadRequestException;
import flab.payment_system.domain.order.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderAdapter orderAdapter;

	@Transactional
	public OrderDto orderProduct(OrderProductDto orderProductDto, Long userId) {
		orderAdapter.checkRemainStock(orderProductDto.productId());
		OrderProduct orderProduct = orderRepository.save(OrderProduct.builder()
			.product(orderAdapter.getProductByProductId(orderProductDto.productId()))
			.user(orderAdapter.getUserByUserId(userId)).quantity(orderProductDto.quantity()).build());
		return new OrderDto(orderProduct.getOrderId());
	}

	public OrderProduct getOrderProductByOrderId(Long orderId) {
		return orderRepository.findById(orderId).orElseThrow(OrderNotExistBadRequestException::new);
	}
}

