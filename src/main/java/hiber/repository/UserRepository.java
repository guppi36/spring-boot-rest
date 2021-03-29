package hiber.repository;

import hiber.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByFirstName(String firstName);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.email = ?1")
    User findByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.id = ?1")
    User findByID(Long id);
}
