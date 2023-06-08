package flab.payment_system.domain.user.repository;

import flab.payment_system.domain.user.domain.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Long>,
	UserVerificationCustomRepository {

}
