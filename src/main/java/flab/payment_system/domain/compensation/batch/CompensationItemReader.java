package flab.payment_system.domain.compensation.batch;


import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import flab.payment_system.domain.payment.response.toss.Settlement;
import flab.payment_system.domain.payment.service.PaymentService;
import flab.payment_system.domain.payment.service.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class CompensationItemReader {
	private final Map<PaymentPgCompany, PaymentStrategy> paymentStrategies;
	private final PaymentService paymentService;

	@Bean
	@StepScope
	public ItemReader<Settlement> paymentStrategyItemReader() {
		return new PaymentStrategyExecutionContextItemReader(paymentStrategies, paymentService);
	}

	public static class PaymentStrategyExecutionContextItemReader implements ItemReader<Settlement> {
		private final Map<PaymentPgCompany, PaymentStrategy> paymentStrategies;
		private final PaymentService paymentService;

		public PaymentStrategyExecutionContextItemReader(Map<PaymentPgCompany, PaymentStrategy> paymentStrategies,
														 PaymentService paymentService) {
			this.paymentStrategies = paymentStrategies;
			this.paymentService = paymentService;
		}

		@Override
		public Settlement read() {
			ExecutionContext executionContext = StepSynchronizationManager.getContext().getStepExecution().getExecutionContext();

			int currentPgCompanyIndex = executionContext.getInt("currentPgCompanyIndex", 0);
			int currentSettlementIndex = executionContext.getInt("currentSettlementIndex", 0);

			PaymentPgCompany[] pgCompanies = paymentStrategies.keySet().toArray(new PaymentPgCompany[0]);

			if (currentPgCompanyIndex >= pgCompanies.length) {
				return null;
			}

			PaymentPgCompany currentPgCompany = pgCompanies[currentPgCompanyIndex];
			Settlement[] settlements = paymentService.getSettlementList(currentPgCompany);

			if (settlements != null && currentSettlementIndex < settlements.length) {

				executionContext.putInt("currentSettlementIndex", currentSettlementIndex + 1);

				if (currentSettlementIndex + 1 >= settlements.length) {
					executionContext.putInt("currentPgCompanyIndex", currentPgCompanyIndex + 1);
					executionContext.putInt("currentSettlementIndex", 0);  // 다음 PG사의 Settlement 인덱스를 0으로 초기화
				}
				return settlements[currentSettlementIndex];
			}
			return null;
		}
	}
}
