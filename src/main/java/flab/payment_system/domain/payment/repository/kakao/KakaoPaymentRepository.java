package flab.payment_system.domain.payment.repository.kakao;

import flab.payment_system.domain.payment.entity.kakao.KakaoPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoPaymentRepository extends JpaRepository<KakaoPayment, Long>,
	KakaoPaymentCustomRepository {
}
