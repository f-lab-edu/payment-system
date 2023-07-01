package flab.payment_system.domain.payment.response.kakao;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PaymentKakaoReadyDtoImpl implements PaymentReadyDto {

	private String tid;
	private String next_redirect_app_url;
	private String next_redirect_mobile_url;
	private String next_redirect_pc_url;
	private String android_app_scheme;
	private String ios_app_scheme;
	private String created_at;
}
