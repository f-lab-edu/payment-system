package flab.payment_system.domain.payment.enums;

public enum PaymentTossEndpoint {
	PAYMENT("/payment"),
	SETTLEMENT("/settlements"),
	APPROVE("/confirm"),
	CANCEL("/cancel");

	private final String endpoint;

	PaymentTossEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
