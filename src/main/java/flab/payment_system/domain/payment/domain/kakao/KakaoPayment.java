package flab.payment_system.domain.payment.domain.kakao;

import flab.payment_system.core.data.BaseEntity;
import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
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
@Table(name = "kakao_payment")
public class KakaoPayment extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "kakao_payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long kakaoPaymentId;

	@Nonnull

	@Column(name = "payment_id", columnDefinition = "BIGINT UNSIGNED")
	private Long paymentId;

	@Embedded
	private CardInfo cardInfo;

	@Embedded
	private Amount amount;

	@Nonnull

	private String aid;

	@Nonnull

	private String paymentMethodType;

	@Builder
	public KakaoPayment(Long paymentId, CardInfo cardInfo, Amount amount, String aid,
		String paymentMethodType) {
		this.paymentId = paymentId;
		this.cardInfo = cardInfo;
		this.amount = amount;
		this.aid = aid;
		this.paymentMethodType = paymentMethodType;
	}
}
