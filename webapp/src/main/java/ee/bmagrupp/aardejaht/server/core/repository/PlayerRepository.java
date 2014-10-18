package ee.bmagrupp.aardejaht.server.core.repository;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.domain.Player;

public interface PlayerRepository extends CrudRepository<Player, Integer> {

	Player findByEmail(String email);
}
