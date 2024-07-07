package flab.payment_system.domain.compensation.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import flab.payment_system.domain.compensation.enums.PGManageName;
import flab.payment_system.domain.compensation.exception.CompensationFileNotfoundException;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;

@Service
public class CompensationFileService {
	private final String basePath;

	@Autowired
	CompensationFileService(@Value("${compensation-path}") String basePath) {
		this.basePath = basePath;
	}
	public Resource getRecentNotFoundFile(PaymentPgCompany pgCompany) {
		PGManageName pgManageName = PGManageName.valueOf(pgCompany.getName().toUpperCase());
		return getRecentFile(pgManageName.getNotFoundFileName(), pgManageName.getPgCompanyName());
	}

	public Resource getRecentInconsistentFile(PaymentPgCompany pgCompany) {
		PGManageName pgManageName = PGManageName.valueOf(pgCompany.getName().toUpperCase());
		return getRecentFile(pgManageName.getInconsistentFileName(), pgManageName.getPgCompanyName());
	}

	private Resource getRecentFile(String fileNamePrefix, String pgCompanyName) {
		try (Stream<Path> files = Files.list(Paths.get(basePath))) {
			Path recentFile = files
				.filter(path -> path.getFileName().toString().startsWith(fileNamePrefix))
				.max(Comparator.comparingLong(path -> path.toFile().lastModified()))
				.orElseThrow(CompensationFileNotfoundException::new);
			return new UrlResource(recentFile.toUri());
		} catch (IOException e) {
			throw new RuntimeException("Failed to retrieve file for " + pgCompanyName, e);
		}
	}
}
