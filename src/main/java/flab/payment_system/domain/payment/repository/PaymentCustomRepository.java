package flab.payment_system.domain.payment.repository;

public interface PaymentCustomRepository {

	long updatePaymentStateByPaymentId(long paymentId, Integer state);

	long updatePaymentStateByOrderId(long paymentId, Integer state);

	long updatePaymentKeyByPaymentId(long paymentId, String paymentKey);

}