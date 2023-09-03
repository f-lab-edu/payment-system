package flab.payment_system.domain.payment.domain.toss;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Failure {

	private String code;
	private String message;

}
