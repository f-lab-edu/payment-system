package flab.payment_system.core.data;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.OffsetDateTime;

@MappedSuperclass
public class BaseEntity {

	@Column(name = "created_at", updatable = false,
		columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime createdAt;

	@Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private OffsetDateTime updatedAt;


	@PrePersist
	public void prePersist() {
		OffsetDateTime now = OffsetDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	public void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
