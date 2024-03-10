package flab.payment_system.domain.product.service;

import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.exception.ProductNotExistBadRequestException;
import flab.payment_system.domain.product.exception.ProductSoldOutException;
import flab.payment_system.domain.product.repository.ProductRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

	private final ProductRepository productRepository;


	@Cacheable(value = "productListCache", key = "#root.methodName + '_' + #lastProductId + '_' + #size", cacheManager = "cacheManager")
	public List<ProductDto> getProductList(Long lastProductId, long size) {
		List<Product> products = productRepository.findByCursor(lastProductId, size);
		return products.stream()
			.map(product -> new ProductDto(product.getProductId(), product.getName(), product.getPrice(), product.getStock()))
			.collect(Collectors.toList());
	}

	public ProductDto getProductDetail(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		return new ProductDto(product.getProductId(), product.getName(), product.getPrice(),
			product.getStock());
	}

	public void checkRemainStock(Long productId) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() == 0) {
			throw new ProductSoldOutException();
		}
	}

	@Transactional
	public void decreaseStock(Long productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		if (product.getStock() - quantity < 0) {
			throw new ProductSoldOutException();
		}
		product.setStock(product.getStock() - quantity);
	}

	@Transactional
	public void increaseStock(Long productId, Integer quantity) {
		Product product = productRepository.findById(productId).orElseThrow(
			ProductNotExistBadRequestException::new);
		product.setStock(product.getStock() + quantity);
	}
}
