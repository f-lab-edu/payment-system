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

	private Integer total;
	@Column(name = "tax_free")
	private Integer taxFree;
	private Integer tax;
	private Integer point;
	private Integer discount;
	@Column(name = "green_deposit")
	private Integer greenDeposit;
}
