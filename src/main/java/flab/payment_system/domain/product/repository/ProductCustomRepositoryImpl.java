package flab.payment_system.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.payment_system.domain.product.domain.QProduct;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProduct product = QProduct.product;

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public long updateDecreaseStock(long productId, Integer quantity) {
		return jpaQueryFactory.update(product).set(product.stock, product.stock.add(-quantity))
			.where(product.productId.eq(productId)).execute();
	}

	@Transactional
	@Modifying(clearAutomatically = true)
	@Override
	public long updateIncreaseStock(long productId, Integer quantity) {
		return jpaQueryFactory.update(product).set(product.stock, product.stock.add(quantity))
			.where(product.productId.eq(productId)).execute();
	}
}
