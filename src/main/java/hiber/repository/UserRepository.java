package hiber.repository;

import hiber.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);
    User findByEmail(String email);
}
