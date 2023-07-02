package flab.payment_system.domain.payment.response.kakao;

import lombok.Getter;
import lombok.ToString;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKakaoReadyDtoImpl implements PaymentReadyDto {

	private String tid;
	private String nextRedirectAppUrl;
	private String nextRedirectMobileUrl;
	private String nextRedirectPcUrl;
	private String androidAppScheme;
	private String iosAppScheme;
	private String createdAt;
	private Long paymentId;

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
}
