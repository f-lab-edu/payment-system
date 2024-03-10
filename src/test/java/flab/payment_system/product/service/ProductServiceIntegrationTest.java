package flab.payment_system.product.service;

import flab.payment_system.config.DatabaseCleanUp;
import flab.payment_system.domain.product.dto.ProductDto;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.exception.ProductNotExistBadRequestException;
import flab.payment_system.domain.product.repository.ProductRepository;
import flab.payment_system.domain.product.service.ProductService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
public class ProductServiceIntegrationTest {

	private final ProductService productService;
	private final ProductRepository productRepository;

	private final DatabaseCleanUp databaseCleanUp;
	private Product product;

	@Autowired
	ProductServiceIntegrationTest
		(ProductService productService, ProductRepository productRepository,
		 DatabaseCleanUp databaseCleanUp) {
		this.productService = productService;
		this.productRepository = productRepository;
		this.databaseCleanUp = databaseCleanUp;

	}

	@BeforeEach
	void setUp() {
		databaseCleanUp.truncateAllEntity();
		product = new Product();
		product.setName("초코파이");
		product.setPrice(1000);
		product.setStock(100);
		productRepository.save(product);
	}

	@AfterEach
	void tearDown() {
		databaseCleanUp.truncateAllEntity();
	}


	@DisplayName("최초 상품목록_조회")
	@Test
	public void getRecentProductListSuccess() {
		// given
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Product product = new Product();
			product.setName("Product" + i);
			product.setPrice(i * 1000);
			product.setStock(i * 10);
			productRepository.save(product);
		});

		Long lastProductId = null;
		long size = 5;


		// when
		List<ProductDto> productList = productService.getProductList(lastProductId, size);

		// then
		assertEquals(size, productList.size(), "조회된 상품 목록의 크기가 요청 크기와 일치해야 합니다.");
		assertTrue(
			IntStream.range(0, productList.size() - 1)
				.allMatch(i -> productList.get(i).productId() > productList.get(i + 1).productId()),
			"상품 목록은 내림차순으로 정렬되어야 합니다."
		);
	}

	@DisplayName("상품목록_조회")
	@Test
	public void getNextProductList() {
		// given
		IntStream.rangeClosed(1, 10).forEach(i -> {
			Product product = new Product();
			product.setName("Product" + i);
			product.setPrice(i * 1000);
			product.setStock(i * 10);
			productRepository.save(product);
		});

		Long lastProductId = 6L;
		long size = 5;


		// when
		List<ProductDto> productList = productService.getProductList(lastProductId, size);


		// then
		assertEquals(size, productList.size(), "조회된 상품 목록의 크기가 요청 크기와 일치해야 합니다.");
		assertTrue(
			IntStream.range(5, productList.size() - 1)
				.allMatch(i -> productList.get(i).productId() > productList.get(i + 1).productId()),
			"상품 목록은 내림차순으로 정렬되어야 합니다."
		);
	}

	@Test
	@DisplayName("상품상세조회_성공")
	void getProductDetailSuccess() {
		// given
		Long productId = 1L;

		// when
		ProductDto result = productService.getProductDetail(productId);

		// then
		assertEquals(result.productId(), product.getProductId());
		assertEquals(result.name(), product.getName());
		assertEquals(result.price(), product.getPrice());
		assertEquals(result.stock(), product.getStock());
	}

	@Test
	@DisplayName("상품상세조회_실패")
	void getProductDetailFailure() {
		// given
		Long invalidProductId = 2L;

		// when & then
		assertThrows(ProductNotExistBadRequestException.class, () -> {
			productService.getProductDetail(invalidProductId);
		});
	}


}
