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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Product product;

	@Nonnull
	private Integer quantity;

	@Builder
	public OrderProduct(User user, Product product, Integer quantity) {
		this.user = user;
		this.product = product;
		this.quantity = quantity;
	}
}
