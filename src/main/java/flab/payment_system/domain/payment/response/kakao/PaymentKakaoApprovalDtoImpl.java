package flab.payment_system.domain.payment.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import flab.payment_system.domain.payment.domain.kakao.Amount;
import flab.payment_system.domain.payment.domain.kakao.CardInfo;
import flab.payment_system.domain.payment.response.PaymentApprovalDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKakaoApprovalDtoImpl implements PaymentApprovalDto, PaymentKakao {

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
