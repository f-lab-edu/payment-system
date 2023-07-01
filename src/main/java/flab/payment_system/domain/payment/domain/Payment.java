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

@Getter
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

	@Builder
	public Payment(Long orderId, String tid, Integer state) {
		this.orderId = orderId;
		this.tid = tid;
		this.state = state;
	}
}
