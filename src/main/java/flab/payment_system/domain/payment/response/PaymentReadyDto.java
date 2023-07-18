package flab.payment_system.domain.payment.response;

public interface PaymentReadyDto {
	void setPaymentId(Long paymentId);
	String getPaymentKey();
}
