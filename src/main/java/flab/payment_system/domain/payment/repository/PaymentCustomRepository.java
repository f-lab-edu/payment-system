package flab.payment_system.domain.payment.repository;

public interface PaymentCustomRepository {

	long updatePaymentStateByPaymentId(long paymentId, Integer state);

	long updateTidByPaymentId(long paymentId, String tid);

}
