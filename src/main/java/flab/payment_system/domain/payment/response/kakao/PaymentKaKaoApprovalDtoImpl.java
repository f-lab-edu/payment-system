package flab.payment_system.domain.payment.response.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKaKaoApprovalDtoImpl implements PaymentApprovalDto {

	private String aid;
	private String tid;
	private String cid;
	private String sid;
	private String partnerOrderId;
	private String partnerUserId;
	private String paymentMethodType;
	private Amount amount;
	private CardInfo cardInfo;
	private String itemName;
	private String itemCode;
	private Integer quantity;
	private Integer taxFreeAmount;
	private Integer vatAmount;
	private String createdAt;
	private String approvedAt;
	private String payload;
}
