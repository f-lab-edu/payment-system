package flab.payment_system.domain.payment.entity.kakao;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CancelAvailableAmount {

	Integer total;
	Integer taxFree;
	Integer vat;
	Integer point;
	Integer discount;
}
