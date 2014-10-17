package ee.bmagrupp.aardejaht.server.service.imlp;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.HomeOwnership;
import ee.bmagrupp.aardejaht.server.core.domain.Ownership;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.OwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.core.repository.UnitRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;
import ee.bmagrupp.aardejaht.server.service.ProfileService;

@Service
public class ProfileServiceImpl implements ProfileService{

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
	public void createSampleData() {
		LOG.info("starting");
		Province prov1 = new Province(58.374684, 26.719168);

		Province prov2 = new Province(58.377261, 26.727258);
		provRepo.save(prov1);
		provRepo.save(prov2);
		LOG.info("provinces saved");
		Player player1 = new Player("Mr. TK", "tk@aardejaht.ee", "abcdef");
		Player player2 = new Player("Doge", "doge@aardejaht.ee", "asddfd");
		playerRepo.save(player1);
		playerRepo.save(player2);
		LOG.info("players saved");

		Unit u = new Unit(2, player1);
		unitRepo.save(u);
		Set<Unit> units = new HashSet<Unit>();
		units.add(u);
		LOG.info("unit saved");
		Ownership o1 = new Ownership(player1, prov1, units);
		ownerRepo.save(o1);
		LOG.info("ownership saved");
		HomeOwnership home = new HomeOwnership(player1, prov2, null);
		homeRepo.save(home);
		LOG.info("home saved");
		
		player1.setHome(home);
		LOG.info("home set");
		Set<Ownership> own = new HashSet<Ownership>();
		own.add(o1);

		player1.setProvinces(own);
		LOG.info("procinces set");

	}

	@Override
	public PlayerProfile getPlayer(int id) {
		LOG.info("trying to find user");
		Player player = playerRepo.findOne(id);
		LOG.info("user here");
		int totalUnits = 0;
		int ownedProvinces = 0;
		
//		LOG.info(player.getProvinces().toString());
		Hibernate.initialize(player.getProvinces());
		Hibernate.initialize(player.getHome());

		LOG.info(player.getProvinces().toString());
		try {
			for (Iterator iterator = player.getProvinces().iterator(); iterator
					.hasNext();) {
				Ownership o = (Ownership) iterator.next();
				ownedProvinces += 1;
				LOG.info("Owned " + ownedProvinces);
				for (Unit unit : o.getUnits()) {
					totalUnits += unit.getSize();
					LOG.info("totalUnits " + totalUnits);
				}

			}
			
			for (Iterator iterator = player.getHome().getUnits().iterator(); iterator
					.hasNext();) {
				Unit unit = (Unit) iterator.next();
				totalUnits += unit.getSize();

			}
			LOG.info("user has " + totalUnits + " at home");



		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new PlayerProfile(player.getId(), player.getUserName(),
				player.getEmail(), totalUnits, ownedProvinces,
				player.getRegisterDate());
	}

	@Override
	public List<Player> findAll() {
		return (List<Player>) playerRepo.findAll();
	}

}
