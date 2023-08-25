package flab.payment_system.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionMessage {

	private String message;
	private int code;

	@Builder
	ExceptionMessage(int code, String message) {
		this.code = code;
		this.message = message;
	}
}
