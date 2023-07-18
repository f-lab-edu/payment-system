package flab.payment_system.config;

import flab.payment_system.core.interceptor.LoggingInterceptor;
import flab.payment_system.domain.payment.enums.PaymentPgCompanyStringToEnumConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoggingInterceptor loggingInterceptor;

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new PaymentPgCompanyStringToEnumConverter());
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loggingInterceptor)
			.order(1)
			.addPathPatterns("/**");
	}
}
