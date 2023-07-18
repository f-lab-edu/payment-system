package flab.payment_system.domain.product.service;

import flab.payment_system.domain.product.domain.Product;
import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.exception.ProductNotExistBadRequestException;
import flab.payment_system.domain.product.exception.ProductSoldOutException;
import flab.payment_system.domain.product.repository.ProductRepository;
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

	public void checkRemainStock(long productId) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() == 0) {
			throw new ProductSoldOutException();
		}
	}

	public void decreaseStock(Long productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() - quantity < 0) {
			throw new ProductSoldOutException();
		}
		productRepository.updateDecreaseStock(productId,
			quantity);
	}

	public void increaseStock(Long productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() - quantity < 0) {
			throw new ProductSoldOutException();
		}
		productRepository.updateIncreaseStock(productId,
			quantity);
	}
}
