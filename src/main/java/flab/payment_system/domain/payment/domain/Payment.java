package flab.payment_system.domain.payment.domain;

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
import lombok.Setter;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "payment")
public class Payment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long paymentId;

	@Column(name = "order_id", columnDefinition = "BIGINT UNSIGNED")
	private Long orderId;

	@Column(columnDefinition = "CHAR(20)")
	private String tid;

	@Nonnull
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

	@Builder
	public Payment(Long orderId, String tid, Integer state, Integer pgCompany, Integer totalAmount,
		Integer taxFreeAmount, Integer installMonth) {
		this.orderId = orderId;
		this.tid = tid;
		this.state = state;
		this.pgCompany = pgCompany;
		this.totalAmount = totalAmount;
		this.taxFreeAmount = taxFreeAmount;
		this.installMonth = installMonth;
	}
}