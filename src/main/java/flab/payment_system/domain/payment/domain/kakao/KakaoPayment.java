package flab.payment_system.domain.payment.domain.kakao;

import flab.payment_system.common.data.BaseEntity;
import flab.payment_system.domain.payment.domain.Payment;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "kakao_payment")
public class KakaoPayment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kakao_payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long kakaoPaymentId;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), unique = true)
	private Payment payment;

	@Embedded
	private CardInfo cardInfo;

	@Column(name = "aid", columnDefinition = "VARCHAR(50)")
	private String aid;

	@Column(name = "payment_method_type", columnDefinition = "VARCHAR(5)")
	private String paymentMethodType;

	@Builder
	public KakaoPayment(Payment payment, CardInfo cardInfo, String aid,
						String paymentMethodType) {
		this.payment = payment;
		this.cardInfo = cardInfo;
		this.aid = aid;
		this.paymentMethodType = paymentMethodType;
	}
}
