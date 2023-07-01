package flab.payment_system.domain.payment.enums;


import org.springframework.core.convert.converter.Converter;

public class PaymentPgCompanyStringToEnumConverter implements Converter<String, PaymentPgCompany> {

	@Override
	public PaymentPgCompany convert(String source) {
		return PaymentPgCompany.valueOf(source.toUpperCase());
	}
}
