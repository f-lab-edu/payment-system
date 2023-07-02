package flab.payment_system.domain.payment.repository;

public interface PaymentCustomRepository {

	long updatePaymentStateByOrderId(long orderId);

	long updateTidByPaymentId(long paymentId, String tid);

}
