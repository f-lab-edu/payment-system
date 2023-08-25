package flab.payment_system.domain.user.repository;

import flab.payment_system.domain.user.entity.UserVerification;
import org.springframework.data.repository.CrudRepository;

public interface UserVerificationRepository extends CrudRepository<UserVerification, Long> {

}
