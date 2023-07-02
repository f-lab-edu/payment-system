package flab.payment_system.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProduct product = QProduct.product;

	@Override
	public long updateDecreaseStock(long productId) {
		return jpaQueryFactory.update(product).set(product.stock, product.stock.add(-1))
			.where(product.productId.eq(productId)).execute();
	}

	@Override
	public long updateIncreaseStock(long productId) {
		return jpaQueryFactory.update(product).set(product.stock, product.stock.add(1))
			.where(product.productId.eq(productId)).execute();
	}
}
