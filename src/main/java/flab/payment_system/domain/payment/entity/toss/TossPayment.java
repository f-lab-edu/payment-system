package flab.payment_system.domain.payment.entity.toss;

import flab.payment_system.common.data.BaseEntity;
import flab.payment_system.domain.payment.entity.Payment;
import jakarta.persistence.*;
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
	private Payment payment;
	@Column(columnDefinition = "VARCHAR(20)")

	private String type;
	@Column(columnDefinition = "VARCHAR(10)")

	private String country;
	@Column(columnDefinition = "VARCHAR(10)")

	private String currency;

	@Builder
	public TossPayment(Payment payment, String type, String country,
					   String currency) {
		this.payment = payment;
		this.type = type;
		this.country = country;
		this.currency = currency;
	}
}
