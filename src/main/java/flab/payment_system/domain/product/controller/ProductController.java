package flab.payment_system.domain.product.controller;

import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 해당 프로젝트의 목표는 결제 시스템을 구현하는 것이기 때문에 Product 도메인은
결제 시스템 구현에 필요한 만큼만 간단히 작성
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/product")
public class ProductController {

	private final ProductService productService;

	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProductDetail(@PathVariable int productId) {
		ProductDto productDto = productService.getProductDetail(productId);

		return ResponseEntity.ok().body(productDto);
	}
}
