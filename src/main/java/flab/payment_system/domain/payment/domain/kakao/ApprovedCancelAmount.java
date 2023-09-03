package flab.payment_system.domain.payment.domain.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ApprovedCancelAmount {

	private Integer total;
	private Integer taxFree;
	private Integer vat;
	private Integer point;
	private Integer discount;
}
