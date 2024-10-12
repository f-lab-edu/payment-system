package flab.payment_system.domain.payment.enums;

public enum PaymentKakaoEndpoint {
	READY("/ready"),
	APPROVE("/approve"),
	CANCEL("/cancel"),
	ORDER("/order");

	private final String endpoint;

	PaymentKakaoEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getEndpoint() {
		return endpoint;
	}
}
