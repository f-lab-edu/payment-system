package flab.payment_system.config;

import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "flab.payment_system")
public class AppConfig {

	@Bean
	public Random random() {
		return new Random(System.currentTimeMillis());
	}
}
