package flab.payment_system.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import flab.payment_system.domain.payment.service.kakao.PaymentStrategyKaKaoService;
import flab.payment_system.domain.payment.service.toss.PaymentStrategyTossService;

@Configuration
public class PaymentStrategyConfig {

	private final PaymentStrategyKaKaoService paymentStrategyKaKaoService;

	private final PaymentStrategyTossService paymentStrategyTossService;

	@Autowired
	public PaymentStrategyConfig(@Lazy PaymentStrategyKaKaoService paymentStrategyKaKaoService,
		@Lazy PaymentStrategyTossService paymentStrategyTossService) {
		this.paymentStrategyKaKaoService = paymentStrategyKaKaoService;
		this.paymentStrategyTossService = paymentStrategyTossService;

	}

	@Bean
	public Map<PaymentPgCompany, PaymentStrategy> paymentStrategies() {
		Map<PaymentPgCompany, PaymentStrategy> paymentStrategies = new HashMap<>();
		paymentStrategies.put(PaymentPgCompany.KAKAO, paymentStrategyKaKaoService);
		paymentStrategies.put(PaymentPgCompany.TOSS, paymentStrategyTossService);
		return paymentStrategies;
	}
}
