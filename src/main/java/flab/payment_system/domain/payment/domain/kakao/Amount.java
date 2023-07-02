package flab.payment_system.domain.payment.response.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Embeddable
public class Amount {

	private int total;
	@Column(name = "tax_free")
	private int taxFree;
	private int tax;
	private int point;
	private int discount;
	@Column(name = "green_deposit")
	private int greenDeposit;
}
