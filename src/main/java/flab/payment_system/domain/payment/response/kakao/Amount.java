package flab.payment_system.domain.payment.response.kakao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Amount {

	private int total;
	private int tax_free;
	private int tax;
	private int point;
	private int discount;
	private int green_deposit;
}
