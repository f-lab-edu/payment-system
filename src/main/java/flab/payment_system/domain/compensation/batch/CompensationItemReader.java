package flab.payment_system.domain.compensation.batch;

import flab.payment_system.domain.payment.dto.PaymentCompensationDto;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CompensationItemReader {
	// pg사 API 를 이용해 데이터를 가져올 것이라고 가정
	@Bean
	@StepScope
	public FlatFileItemReader<PaymentCompensationDto> paymentKakaoItemReader() {
		return getItemReader("payment_kakao.csv");
	}

	@Bean
	@StepScope
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
		delimitedLineTokenizer.setNames("orderId", "paymentKey", "totalAmount", "paymentState", "taxFreeAmount",
			"installMonth");
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

		BeanWrapperFieldSetMapper<PaymentCompensationDto> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
		beanWrapperFieldSetMapper.setTargetType(PaymentCompensationDto.class);

		defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

		flatFileItemReader.setLineMapper(defaultLineMapper);

		return flatFileItemReader;
	}
}
