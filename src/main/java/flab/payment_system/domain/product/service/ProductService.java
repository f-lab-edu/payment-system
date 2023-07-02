package flab.payment_system.domain.product.service;

import flab.payment_system.domain.order.dto.OrderProductDto;
import flab.payment_system.domain.product.domain.Product;
import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.exception.ProductNotExistBadRequestException;
import flab.payment_system.domain.product.exception.ProductSoldOutException;
import flab.payment_system.domain.product.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;


	public ProductDto getProductDetail(long productId) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		return new ProductDto(product.getProductId(), product.getName(), product.getPrice(),
			product.getStock());
	}

	private void checkRemainStock(OrderProductDto orderProductDto) {
		Product product = productRepository.findById(orderProductDto.productId()).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() == 0) throw new ProductSoldOutException();
	}

	private void decreaseStock(OrderProductDto orderProductDto) {
		productRepository.updateDecreaseStock(orderProductDto.productId());
	}
	public void increaseStock(long productId) {
		productRepository.updateIncreaseStock(productId);
	}

	@Transactional
	public void transactionDecreaseStock(OrderProductDto orderProductDto) {
		checkRemainStock(orderProductDto);
		decreaseStock(orderProductDto);
	}
}
