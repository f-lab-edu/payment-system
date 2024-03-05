package flab.payment_system.domain.payment.repository;

public interface PaymentCustomRepository {

	long updatePaymentStateByPaymentId(Long paymentId, Integer state);

	long updatePaymentStateByOrderId(Long paymentId, Integer state);

}
