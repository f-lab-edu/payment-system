package flab.payment_system.domain.order.service;

import flab.payment_system.domain.order.entity.OrderProduct;
import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.order.repository.OrderRepository;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class OrderService {

	private final OrderRepository orderRepository;

	@Transactional
	public Long orderProduct(OrderProductDto orderProductDto, Long userId) {
		AtomicReference<Integer> installMonth = new AtomicReference<>(0);
		Optional<Integer> optionalInstallMonth = orderProductDto.getInstallMonth();
		optionalInstallMonth.ifPresent(installMonth::set);

		OrderProduct orderProduct = orderRepository.save(
			OrderProduct.builder().productId(orderProductDto.productId()).userId(userId)
				.quantity(orderProductDto.quantity()).build());
		return orderProduct.getOrderId();
	}
}

