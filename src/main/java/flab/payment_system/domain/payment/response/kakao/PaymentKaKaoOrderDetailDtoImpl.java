package flab.payment_system.domain.payment.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import flab.payment_system.domain.payment.domain.kakao.Amount;
import flab.payment_system.domain.payment.domain.kakao.CancelAvailableAmount;
import flab.payment_system.domain.payment.domain.kakao.CanceledAmount;
import flab.payment_system.domain.payment.domain.kakao.PaymentActionDetails;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKaKaoOrderDetailDtoImpl implements PaymentOrderDetailDto, PaymentKaKao {

	String tid;
	String cid;
	String status;
	String partnerOrderId;
	String partnerUserId;
	String paymentMethodType;
	String itemName;
	Integer quantity;
	Amount amount;
	CanceledAmount canceledAmount;
	CancelAvailableAmount cancelAvailableAmount;
	String createdAt;
	String approvedAt;
	String canceledAt;
	PaymentActionDetails[] paymentActionDetails;
}
