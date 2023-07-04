package flab.payment_system.domain.payment.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import flab.payment_system.domain.payment.response.PaymentReadyDto;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKakaoReadyDtoImpl implements PaymentReadyDto, PaymentKakao {

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

	public String getPaymentKey() {
		return this.tid;
	}
}
