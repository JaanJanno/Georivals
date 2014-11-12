package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.domain.UnitState;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.rest.domain.PlayerProfile;
import ee.bmagrupp.georivals.server.service.ProfileService;
import ee.bmagrupp.georivals.server.util.GeneratorUtil;

@Service
public class ProfileServiceImpl implements ProfileService {

	private static Logger LOG = LoggerFactory
			.getLogger(ProfileServiceImpl.class);
	@Autowired
	ProvinceRepository provRepo;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	OwnershipRepository ownerRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Autowired
	UnitRepository unitRepo;

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
		int totalUnits = 0;
		int ownedProvinces = 0;

		for (Ownership o : player.getOwnedProvinces()) {

			ownedProvinces += 1;
			totalUnits += countUnits(o.getUnits());

		}

		if (player.getHome() != null) {
			totalUnits += countUnits(player.getHome().getUnits());
		}

		return new PlayerProfile(player.getId(), player.getUserName(),
				player.getEmail(), totalUnits, ownedProvinces);
	}

	private int countUnits(Set<Unit> units) {
		int unitCount = 0;
		if (units != null) {
			for (Unit unit : units) {
				if(unit.getState() == UnitState.CLAIMED){
					unitCount += unit.getSize();
				}
			}
		}
		return unitCount;
	}

	/**
	 * This method is left in as a reminder of how to generate sample data.
	 * 
	 * @see ee.bmagrupp.georivals.server.service.ProfileService#createSampleData()
	 */
	@Override
	public void createSampleData() {
		LOG.info("starting");
		Province prov1 = new Province(58.374684, 26.719168);
		Province prov2 = new Province(58.377261, 26.727258);
		Province prov3 = new Province(59.338334, 26.358376);
		Province prov4 = new Province(58.917478, 25.620758);
		Province prov5 = new Province(58.374380, 26.749884);
		Province prov6 = new Province(58.357904, 26.682507);
		provRepo.save(prov1);
		provRepo.save(prov2);
		provRepo.save(prov3);
		provRepo.save(prov4);
		provRepo.save(prov5);
		provRepo.save(prov6);
		LOG.info("provinces saved");
		Player player1 = new Player("Mr. TK", "mr.tk@pacific.ee",
				GeneratorUtil.generateString(6), prov1);
		Player player2 = new Player("Doge", "doge@pacific.ee",
				GeneratorUtil.generateString(6), prov1);
		Player player3 = new Player("Biitnik", "biitnik@pacific.ee",
				GeneratorUtil.generateString(6), prov2);
		Player player4 = new Player("Spinning S-man", "spinning@pacific.ee",
				GeneratorUtil.generateString(6), prov3);
		Player player5 = new Player("JohnnyZQ", "johnnyzq@pacific.ee",
				GeneratorUtil.generateString(6), prov4);
		Player player6 = new Player("King Jaan", "kingjaan@pacific.ee",
				GeneratorUtil.generateString(6), prov5);
		LOG.info("players created");
		homeRepo.save(player1.getHome());
		homeRepo.save(player2.getHome());
		homeRepo.save(player3.getHome());
		homeRepo.save(player4.getHome());
		homeRepo.save(player5.getHome());
		homeRepo.save(player6.getHome());

		LOG.info("player homes saved");

		Unit u1 = new Unit(4);
		Unit u2 = new Unit(2);
		Unit u3 = new Unit(6);
		Unit u4 = new Unit(3);
		Unit u5 = new Unit(1);
		Unit u6 = new Unit(9);
		Unit u7 = new Unit(14);
		Unit u8 = new Unit(5);

		unitRepo.save(u1);
		unitRepo.save(u2);
		unitRepo.save(u3);
		unitRepo.save(u4);
		unitRepo.save(u5);
		unitRepo.save(u6);
		unitRepo.save(u7);
		unitRepo.save(u8);
		LOG.info("units created and saved");

		player1.addOwnership(new Ownership(prov1, u1));
		player2.addOwnership(new Ownership(prov2, u2));
		player3.addOwnership(new Ownership(prov3, u3));
		player4.addOwnership(new Ownership(prov4, u4));
		player5.addOwnership(new Ownership(prov5, u5));
		player1.addOwnership(new Ownership(prov6, u6));
		LOG.info("ownerships added");
		ownerRepo.save(player1.getOwnedProvinces());
		ownerRepo.save(player2.getOwnedProvinces());
		ownerRepo.save(player3.getOwnedProvinces());
		ownerRepo.save(player4.getOwnedProvinces());
		ownerRepo.save(player5.getOwnedProvinces());
		ownerRepo.save(player6.getOwnedProvinces());
		LOG.info("ownerships saved");
		player6.getHome().addUnit(u7);
		player5.getHome().addUnit(u8);
		LOG.info("home units added");

		playerRepo.save(player1);
		playerRepo.save(player2);
		playerRepo.save(player3);
		playerRepo.save(player4);
		playerRepo.save(player5);
		playerRepo.save(player6);

		LOG.info("players saved");

	}

}
