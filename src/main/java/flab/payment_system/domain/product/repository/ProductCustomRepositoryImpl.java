package flab.payment_system.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProduct product = QProduct.product;

	@Override
	public List<Product> findByCursor(Long lastProductId, long size) {
		var query = jpaQueryFactory.selectFrom(product)
			.orderBy(product.productId.desc())
			.limit(size);

		if (lastProductId != null) {
			query.where(product.productId.lt(lastProductId));
		}

		return query.fetch();
	}
}
