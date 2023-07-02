package flab.payment_system.domain.payment.domain.kakao;

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
