package flab.payment_system.domain.user.entity;


import flab.payment_system.common.data.BaseEntity;
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
	private String email;

	@NonNull
	@Column(columnDefinition = "CHAR(60)")
	private String password;

	@Builder
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
