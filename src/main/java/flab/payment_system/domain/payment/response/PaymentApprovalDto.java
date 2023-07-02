package flab.payment_system.domain.payment.response;

import flab.payment_system.domain.payment.domain.kakao.Amount;
import flab.payment_system.domain.payment.domain.kakao.CardInfo;

public interface PaymentApprovalDto {

	String getAid();

	String getTid();

	String getCid();

	String getSid();

	String getPartnerOrderId();

	String getPartnerUserId();

	String getPaymentMethodType();

	Amount getAmount();

	CardInfo getCardInfo();

	String getItemName();

	String getItemCode();

	Integer getQuantity();

	Integer getTaxFreeAmount();

	Integer getVatAmount();

	String getCreatedAt();

	String getApprovedAt();

	String getPayload();

}
