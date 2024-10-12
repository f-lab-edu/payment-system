package flab.payment_system.domain.payment.response.toss;

import flab.payment_system.domain.payment.entity.toss.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class Settlement implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
	String mId;
	String paymentKey;
	String transactionKey;
	String orderId;
	String currency;
	String method;
	Integer amount;
	Integer interestFee;
	Fee[] fees;
	Integer supplyAmount;
	Integer vat;
	Integer payOutAmount;
	String approvedAt;
	String soldDate;
	String paidOutDate;
	Card card;
	EasyPay easyPay;
	GiftCertificate giftCertificate;
	MobilePhone mobilePhone;
	Transfer transfer;
	VirtualAccount virtualAccount;
	Cancels cancels;
};
