package flab.payment_system.domain.payment.domain.toss;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Card {

	private Integer amount;
	private String issuerCode;
	private String acquirerCode;
	private String number;
	private Integer installmentPlanMonths;
	private String approveNo;
	private boolean useCardPoint;
	private String cardType;
	private String ownerType;
	private String acquireStatus;
	private String isInterestFree;
	private String interestPayer;
}
