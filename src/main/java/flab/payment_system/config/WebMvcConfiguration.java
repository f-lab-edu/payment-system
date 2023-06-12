package flab.payment_system.config;

import flab.payment_system.core.interceptor.IpAddressRangeCheckControllerInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {

	private final IpAddressRangeCheckControllerInterceptor ipAddressRangeCheckControllerInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(ipAddressRangeCheckControllerInterceptor)
			.order(1)
			.addPathPatterns("/users/sign-in")
			.addPathPatterns("/users/e-mail");
	}
}
