package ee.bmagrupp.georivals.server.game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;

@Service
public class PlayerServiceImpl implements PlayerService {

	@Autowired
	MovementRepository movRepo;

	@Override
	public int calculatePlayerStrength(Player player) {
		int stationary = player.findPlayerUnitCount();

		int moving = 0;
		List<Movement> movements = movRepo.findByPlayerSid(player.getSid());
		for (Movement movement : movements) {
			moving += movement.getUnit().getSize();
		}
		return stationary + moving;
	}

}
