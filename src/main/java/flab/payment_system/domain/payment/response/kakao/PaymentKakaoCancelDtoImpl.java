package flab.payment_system.domain.payment.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import flab.payment_system.domain.payment.domain.kakao.Amount;
import flab.payment_system.domain.payment.domain.kakao.ApprovedCancelAmount;
import flab.payment_system.domain.payment.domain.kakao.CancelAvailableAmount;
import flab.payment_system.domain.payment.domain.kakao.CanceledAmount;
import flab.payment_system.domain.payment.response.PaymentCancelDto;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKakaoCancelDtoImpl implements PaymentCancelDto, PaymentKakao {

	private String cid;
	private String tid;
	private String aid;
	private String status;
	private String partnerOrderId;
	private String partnerUserId;
	private String paymentMethodType;
	private Amount amount;
	private ApprovedCancelAmount approvedCancelAmount;
	private CanceledAmount canceledAmount;
	private CancelAvailableAmount cancelAvailableAmount;
	private String itemName;
	private String itemCode;
	private Integer quantity;
	private String createdAt;
	private String approvedAt;
	private String canceledAt;
}
