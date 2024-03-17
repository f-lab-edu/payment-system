package flab.payment_system.domain.payment.response.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import flab.payment_system.domain.payment.entity.kakao.Amount;
import flab.payment_system.domain.payment.entity.kakao.CancelAvailableAmount;
import flab.payment_system.domain.payment.entity.kakao.CanceledAmount;
import flab.payment_system.domain.payment.entity.kakao.PaymentActionDetails;
import flab.payment_system.domain.payment.response.PaymentOrderDetailDto;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaymentKakaoOrderDetailDtoImpl implements PaymentOrderDetailDto, PaymentKakao {

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
