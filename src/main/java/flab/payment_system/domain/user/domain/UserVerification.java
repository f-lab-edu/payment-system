package flab.payment_system.domain.user.domain;


import flab.payment_system.core.data.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.nullness.qual.NonNull;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "verification")
public class UserVerification extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "verification_id", columnDefinition = "BIGINT UNSIGNED")
	private Long verificationId;

	@NonNull
	@Column(columnDefinition = "INT UNSIGNED")
	private Integer verificationNumber;

	@NonNull
	@Column(name = "e-mail", columnDefinition = "VARCHAR(350)")
	private String email;

	@NonNull
	@Column(columnDefinition = "boolean default false")
	private boolean isVerified;

	@Builder
	public UserVerification(Long userId, String email, Integer verificationNumber,
		boolean isVerified) {
		this.email = email;
		this.verificationNumber = verificationNumber;
		this.isVerified = isVerified;
	}
}
