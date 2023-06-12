package flab.payment_system.domain.user.repository;

import flab.payment_system.domain.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserCustomRepository {
	 Optional<User> findByEmail(String email);
}
