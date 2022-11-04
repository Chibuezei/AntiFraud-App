package antifraud.persist;

import antifraud.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByOrderById();
    List<User> findByNameOrderByName(String name);
    User findByUsernameIgnoreCase(String username);
}
