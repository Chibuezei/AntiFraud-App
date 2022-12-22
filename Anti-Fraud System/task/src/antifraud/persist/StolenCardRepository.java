package antifraud.persist;

import antifraud.entities.StolenCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {

    Optional<StolenCard> findByNumber(String number);

    List<StolenCard> findAllByOrderById();
}
