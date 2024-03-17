package flab.payment_system.domain.payment.domain;

import flab.payment_system.common.data.BaseEntity;
import flab.payment_system.domain.order.entity.OrderProduct;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long paymentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), unique = true)
	private OrderProduct orderProduct;

	@Nonnull
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private Integer state;

	// 0 카카오, 1 토스
	@NonNull
	@Column(name = "pgCompany", columnDefinition = "TINYINT UNSIGNED")
	private Integer pgCompany;

	@Nonnull
	private Integer totalAmount;

	@Nonnull
	private Integer taxFreeAmount;

	@Nonnull
	private Integer installMonth;


	@Column(columnDefinition = "VARCHAR(200)")
	private String paymentKey;

	@Builder
	public Payment(OrderProduct orderProduct, Integer state, Integer pgCompany, Integer totalAmount,
				   Integer taxFreeAmount, Integer installMonth, String paymentKey) {
		this.orderProduct = orderProduct;
		this.state = state;
		this.pgCompany = pgCompany;
		this.totalAmount = totalAmount;
		this.taxFreeAmount = taxFreeAmount;
		this.installMonth = installMonth;
		this.paymentKey = paymentKey;
	}

	public void setPaymentKey(String paymentKey) {
		this.paymentKey = paymentKey;
	}

	public void setState(Integer state) {
		this.state = state;
	}
}
