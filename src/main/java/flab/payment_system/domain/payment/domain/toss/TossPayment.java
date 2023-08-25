package flab.payment_system.domain.payment.domain.toss;

import flab.payment_system.common.data.BaseEntity;
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
@Table(name = "toss_payment")
public class TossPayment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "toss_payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long tossPaymentId;

	@Nonnull
	@Column(name = "payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long paymentId;
	@Column(columnDefinition = "VARCHAR(20)")

	private String type;
	@Column(columnDefinition = "VARCHAR(10)")

	private String country;
	@Column(columnDefinition = "VARCHAR(10)")

	private String currency;

	@Builder
	public TossPayment(Long paymentId, String type, String country,
		String currency) {
		this.paymentId = paymentId;
		this.type = type;
		this.country = country;
		this.currency = currency;
	}
}
