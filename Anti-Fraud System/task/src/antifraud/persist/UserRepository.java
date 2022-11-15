package antifraud.persist;

import antifraud.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByOrderById();
    List<User> findByNameOrderByName(String name);

    @Override
    Optional<User> findById(Long aLong);

    Optional<User> findByUsernameIgnoreCase(String username);
}
