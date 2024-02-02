package flab.payment_system.domain.user.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash(value = "verification", timeToLive = 1200)
public class UserVerification {

	@Id
	private Long verificationId;

	private Integer verificationNumber;

	private String email;

	private boolean isVerified;

	@Builder
	public UserVerification(Long verificationId, Long userId, String email,
		Integer verificationNumber,
		boolean isVerified) {
		this.verificationId = verificationId;
		this.email = email;
		this.verificationNumber = verificationNumber;
		this.isVerified = isVerified;
	}
}
