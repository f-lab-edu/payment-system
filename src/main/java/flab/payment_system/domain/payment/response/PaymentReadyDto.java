package flab.payment_system.domain.payment.response;

public interface PaymentReadyDto {

	String getTid();

	void setPaymentId(Long paymentId);
}
