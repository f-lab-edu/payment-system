package flab.payment_system.domain.compensation.batch.correction;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import flab.payment_system.domain.payment.dto.PaymentCompensationDto;
import flab.payment_system.domain.payment.entity.Payment;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CorrectionFileWriter {
	private final String basePath;

	@Autowired
	CorrectionFileWriter(@Value("${compensation-path}") String basePath) {
		this.basePath = basePath;
	}

	public void writeNotFoundPaymentForCorrection(List<PaymentCompensationDto> dtos, String fileName) {
		createDirectoryIfNotExists(basePath);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath(fileName), true))) {
			for (PaymentCompensationDto dto : dtos) {
				String data = String.format("%d,%s,%d,%d,%d,%d%n",
					dto.getOrderId(), dto.getPaymentKey(), dto.getTotalAmount(),
					dto.getPaymentState(), dto.getTaxFreeAmount(), dto.getInstallMonth());
				writer.write(data);
			}
		} catch (IOException e) {
			log.error("Error writing correction to file", e);
		}
	}

	public void writeInconsistentPaymentForCorrection(List<Payment> payments, String fileName) {
		createDirectoryIfNotExists(basePath);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(getFilePath(fileName), true))) {
			for (Payment payment : payments) {
				String data = String.format("%d,%s,%d,%d,%d,%d%n",
					payment.getOrderProduct().getOrderId(), payment.getPaymentKey(), payment.getTotalAmount(),
					payment.getState(), payment.getTaxFreeAmount(), payment.getInstallMonth());
				writer.write(data);
			}
		} catch (IOException e) {
			log.error("Error writing correction to file", e);
		}
	}

	private String getFilePath(String fileName) {
		return basePath + fileName;
	}

	private void createDirectoryIfNotExists(String directoryPath) {
		Path path = Paths.get(directoryPath);
		if (!Files.exists(path)) {
			try {
				Files.createDirectories(path);
			} catch (IOException e) {
				log.error("Error creating directory: {}", directoryPath, e);
			}
		}
	}

}
