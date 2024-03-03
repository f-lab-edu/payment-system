package flab.payment_system.domain.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import flab.payment_system.domain.product.entity.QProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QProduct product = QProduct.product;
}
