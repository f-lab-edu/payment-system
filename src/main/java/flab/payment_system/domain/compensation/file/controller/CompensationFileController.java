package flab.payment_system.domain.compensation.file.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import flab.payment_system.domain.compensation.file.service.CompensationFileService;
import flab.payment_system.domain.payment.enums.PaymentPgCompany;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/compensation")
public class CompensationFileController {
	private final CompensationFileService compensationFileService;

	@GetMapping("/{pgCompany}/not-found-file")
	public ResponseEntity<Resource> downloadNotFoundFile(@PathVariable PaymentPgCompany pgCompany) {
		Resource fileResource = compensationFileService.getRecentNotFoundFile(pgCompany);
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
			.body(fileResource);
	}

	@GetMapping("/{pgCompany}/inconsistent-file")
	public ResponseEntity<Resource> downloadInconsistentFile(@PathVariable PaymentPgCompany pgCompany) {
		Resource fileResource = compensationFileService.getRecentInconsistentFile(pgCompany);
		return ResponseEntity.ok()
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
			.body(fileResource);
	}
}
