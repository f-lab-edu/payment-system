package flab.payment_system.domain.order.domain;

import flab.payment_system.core.data.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "order_product")
public class Order extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id", columnDefinition = "BIGINT UNSIGNED")
	private Long orderId;

	@Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED")
	private Long userId;

	@NonNull
	@Column(name = "productId", columnDefinition = "BIGINT UNSIGNED")
	private Long productId;

	// 0 카카오, 1 토스
	@NonNull
	@Column(name = "pgCompany", columnDefinition = "TINYINT UNSIGNED")
	private Integer pgCompany;

	@Nonnull
	private Integer quantity;

	@Nonnull
	private Integer totalAmount;

	@Nonnull
	private Integer taxFreeAmount;

	@Nonnull
	private Integer installMonth;

	@Builder
	public Order(long userId, long productId, Integer pgCompany,
		Integer quantity, Integer totalAmount, Integer taxFreeAmount, Integer installMonth) {
		this.userId = userId;
		this.productId = productId;
		this.pgCompany = pgCompany;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
		this.taxFreeAmount = taxFreeAmount;
		this.installMonth = installMonth;
	}
}
