package flab.payment_system.domain.batch.compensation;

import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CompensationItemReader {
	@Bean
	public FlatFileItemReader<PaymentCompensationDto> paymentKakaoItemReader() {
		return getItemReader("payment_kakao.csv");
	}

	@Bean
	public FlatFileItemReader<PaymentCompensationDto> paymentTossItemReader() {
		return getItemReader("payment_toss.csv");
	}

	private FlatFileItemReader<PaymentCompensationDto> getItemReader(String fileName) {
		FlatFileItemReader<PaymentCompensationDto> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource(fileName));
		flatFileItemReader.setLinesToSkip(1);
		flatFileItemReader.setEncoding("UTF-8");

		DefaultLineMapper<PaymentCompensationDto> defaultLineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
		delimitedLineTokenizer.setNames("orderId", "paymentKey", "totalAmount", "paymentState", "taxFreeAmount", "installMonth");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<PaymentCompensationDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(PaymentCompensationDto.class);

		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);

		return flatFileItemReader;
	}
}
