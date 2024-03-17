package flab.payment_system.domain.payment.entity.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Embeddable
public class CardInfo {

	@Column(name = "interest_free_install", columnDefinition = "VARCHAR(5)")
	private String interestFreeInstall;
	@Column(columnDefinition = "VARCHAR(10)")
	private String bin;
	@Column(name = "card_type", columnDefinition = "VARCHAR(10)")
	private String cardType;
	@Column(name = "card_mid", columnDefinition = "VARCHAR(20)")
	private String cardMid;
	@Column(name = "approved_id", columnDefinition = "VARCHAR(20)")
	private String approvedId;
	@Column(name = "install_month", columnDefinition = "VARCHAR(5)")
	private String installMonth;
	@Column(name = "purchase_corp", columnDefinition = "VARCHAR(10)")
	private String purchaseCorp;
	@Column(name = "purchase_corp_code", columnDefinition = "VARCHAR(10)")
	private String purchaseCorpCode;
	@Column(name = "issure_corp", columnDefinition = "VARCHAR(10)")
	private String issuerCorp;
	@Column(name = "issuer_corp_code", columnDefinition = "VARCHAR(10)")
	private String issuerCorpCode;
	@Column(name = "kakaopay_purchase_corp", columnDefinition = "VARCHAR(10)")
	private String kakaopayPurchaseCorp;
	@Column(name = "kakaopay_purchase_corp_code", columnDefinition = "VARCHAR(10)")
	private String kakaopayPurchaseCorpCode;
	@Column(name = "kakaopay_issuer_corp", columnDefinition = "VARCHAR(10)")
	private String kakaopayIssuerCorp;
	@Column(name = "kakaopay_issuer_corp_code", columnDefinition = "VARCHAR(10)")
	private String kakaopayIssuerCorpCode;
}
