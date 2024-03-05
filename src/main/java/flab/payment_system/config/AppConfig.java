package flab.payment_system.config;


import flab.payment_system.common.enums.Constant;
import flab.payment_system.common.filter.ExceptionHandlerFilter;
import flab.payment_system.common.filter.SignInCheckFilter;
import flab.payment_system.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

	private final SessionService sessionService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http
			.csrf(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests((authorizeRequests) -> {
				authorizeRequests.anyRequest().permitAll();
			})

			.formLogin(AbstractHttpConfigurer::disable)

			.build();
	}

	@Bean
	public FilterRegistrationBean<OncePerRequestFilter> SignInCheckFilter() {

		FilterRegistrationBean<OncePerRequestFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(
			new SignInCheckFilter(sessionService));
		bean.setOrder(2);
		bean.addUrlPatterns(
			Constant.API_AND_VERSION.getValue() + "/order/*",
			Constant.API_AND_VERSION.getValue() + "/payments/*");

		return bean;
	}

	@Bean
	public FilterRegistrationBean<OncePerRequestFilter> ExceptionHandlerFilter() {

		FilterRegistrationBean<OncePerRequestFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(
			new ExceptionHandlerFilter());
		bean.setOrder(1);
		bean.addUrlPatterns(
			Constant.API_AND_VERSION.getValue() + "/order/*",
			Constant.API_AND_VERSION.getValue() + "/payments/*");

		return bean;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
	@Bean
	HttpClient httpClient() {
		return HttpClientBuilder.create()
			.setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
				.setMaxConnPerRoute(30)
				.setMaxConnTotal(60)
				.build())
			.build();
	}

	@Bean
	HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout(3000);
		factory.setHttpClient(httpClient);

		return factory;
	}

	@Bean
	RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory factory) {
		return new RestTemplate(factory);
	}


}
