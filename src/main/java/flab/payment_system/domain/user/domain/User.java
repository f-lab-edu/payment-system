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
@Table(name = "user")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", columnDefinition = "BIGINT UNSIGNED")
	private Long userId;

	@NonNull
	@Column(name = "e-mail", columnDefinition = "VARCHAR(350)")
	private String eMail;

	@NonNull
	@Column(columnDefinition = "CHAR(60)")
	private String password;

	@Builder
	public User(String eMail, String password) {
		this.eMail = eMail;
		this.password = password;
	}
}
