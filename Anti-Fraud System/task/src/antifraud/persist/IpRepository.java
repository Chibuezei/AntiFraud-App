package antifraud.persist;

import antifraud.entities.Ip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IpRepository extends JpaRepository<Ip, Long> {

    Optional<Ip> findByIp(String Ip);

    List<Ip> findAllByOrderById();

}
