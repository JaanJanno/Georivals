package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.game.PlayerService;
import ee.bmagrupp.georivals.server.rest.domain.PlayerProfile;
import ee.bmagrupp.georivals.server.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileServiceImpl.class);

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	PlayerService playerServ;

	@Override
	public PlayerProfile getPlayerProfile(int id) {
		LOG.info("trying to find user");
		Player player = playerRepo.findOne(id);
		LOG.info("user here");
		return createProfileEntry(player);
	}

	@Override
	public List<PlayerProfile> findAll() {
		List<Player> pList = (List<Player>) playerRepo.findAll();

		List<PlayerProfile> profiles = new ArrayList<>();
		for (Player player : pList) {
			profiles.add(createProfileEntry(player));
		}
		return profiles;
	}

	@Override
	public PlayerProfile getPlayerProfile(String sid) {
		Player player = playerRepo.findBySid(sid);
		return createProfileEntry(player);
	}

	private PlayerProfile createProfileEntry(Player player) {
		if (player == null)
			return null;
		int totalUnits = playerServ.calculatePlayerStrength(player);
		// +1 for home province
		int ownedProvinces = player.getOwnedProvinces().size() + 1;

		return new PlayerProfile(player.getId(), player.getUserName(),
				player.getEmail(), totalUnits, ownedProvinces);
	}

}
