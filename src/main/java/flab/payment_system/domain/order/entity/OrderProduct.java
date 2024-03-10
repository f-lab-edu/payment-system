package flab.payment_system.domain.order.entity;

import flab.payment_system.common.data.BaseEntity;
import flab.payment_system.domain.product.entity.Product;
import flab.payment_system.domain.user.entity.User;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_product")
public class OrderProduct extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", columnDefinition = "BIGINT UNSIGNED")
	private Long orderId;

	@Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED")
	private Long userId;

	@NonNull
	@Column(name = "productId", columnDefinition = "BIGINT UNSIGNED")
	private Long productId;

	@Nonnull
	private Integer quantity;

	@Builder
	public OrderProduct(long userId, long productId, Integer quantity) {
		this.userId = userId;
		this.productId = productId;
		this.quantity = quantity;
	}
}
