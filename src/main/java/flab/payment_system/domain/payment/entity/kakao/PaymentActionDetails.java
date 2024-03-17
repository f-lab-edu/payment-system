package flab.payment_system.domain.payment.entity.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentActionDetails {

	private String aid;
	private String paymentActionType;
	private String paymentMethodType;
	private Integer amount;
	private Integer pointAmount;
	private Integer discountAmount;
	private String approvedAt;
}
