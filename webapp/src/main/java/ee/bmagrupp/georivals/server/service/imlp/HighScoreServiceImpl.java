package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.domain.UnitState;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.rest.domain.HighScoreEntry;
import ee.bmagrupp.georivals.server.service.HighScoreService;

@Service
public class HighScoreServiceImpl implements HighScoreService {

	private static Logger LOG = LoggerFactory
			.getLogger(HighScoreServiceImpl.class);

	@Autowired
	PlayerRepository playerRepo;

	@Override
	public HighScoreEntry findById(int id) {
		LOG.info("Creating highscore for player " + id);
		Player player = playerRepo.findOne(id);
		LOG.info("player found");
		return createHighScore(player);
	}

	@Override
	public List<HighScoreEntry> findAll() {
		LOG.info("Creating highscore for all Players");
		List<Player> pList = (List<Player>) playerRepo.findAll();
		List<HighScoreEntry> highList = new ArrayList<>();
		for (Player player : pList) {
			highList.add(createHighScore(player));
		}
		return highList;
	}

	private HighScoreEntry createHighScore(Player player) {
		double provinces = 0;
		double units = 0;
		double average = 0;

		if (player.getOwnedProvinces() != null) {
			for (Ownership own : player.getOwnedProvinces()) {
				provinces += 1;
				if (own.getUnits() != null) {
					for (Unit unit : own.getUnits()) {
						if(unit.getState() == UnitState.CLAIMED){
							units += unit.getSize();
						}
					}
				}
			}

		}

		if (player.getHome().getUnits() != null) {
			for (Unit unit : player.getHome().getUnits()) {
				if(unit.getState() == UnitState.CLAIMED){
					units += unit.getSize();
				}
			}

		}

		if (provinces != 0) {
			average = units / provinces;
		}

		return new HighScoreEntry(player.getId(), player.getUserName(),
				average, (int) provinces);
	}

}
