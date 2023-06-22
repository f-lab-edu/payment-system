package flab.payment_system.config;


import flab.payment_system.core.filter.ExceptionHandlerFilter;
import flab.payment_system.core.filter.SignInCheckFilter;
import flab.payment_system.domain.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtService jwtService;

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
			new SignInCheckFilter(jwtService));
		bean.setOrder(2);
		bean.addUrlPatterns("/users/test", "/payments/*");

		return bean;
	}

	@Bean
	public FilterRegistrationBean<OncePerRequestFilter> ExceptionHandlerFilter() {

		FilterRegistrationBean<OncePerRequestFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(
			new ExceptionHandlerFilter());
		bean.setOrder(1);
		bean.addUrlPatterns("/users/test", "/payments/*");

		return bean;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
}
