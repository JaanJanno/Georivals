package ee.bmagrupp.georivals.server.service.imlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.RegistrationService;
import ee.bmagrupp.georivals.server.util.CalculationUtil;
import ee.bmagrupp.georivals.server.util.Constants;
import ee.bmagrupp.georivals.server.util.GeneratorUtil;
import ee.bmagrupp.georivals.server.util.ServerResult;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private HomeOwnershipRepository homeRepo;

	@Autowired
	private ProvinceRepository provRepo;

	@Autowired
	private UnitRepository unitRepo;

	@Override
	public ServerResponse registrationPhase1(RegistrationDTO dto) {
		Player player = playerRepo.findByUserName(dto.getUserName());
		if (player == null) {
			return new ServerResponse(ServerResult.OK);
		} else {
			return new ServerResponse(ServerResult.USERNAME_IN_USE);
		}

	}

	@Override
	public ServerResponse registrationPhase2(RegistrationDTO dto) {
		Player player = playerRepo.findByUserName(dto.getUserName());
		if (player != null) {
			return new ServerResponse(ServerResult.USERNAME_IN_USE);
		}
		player = createPlayer(dto.getUserName(), dto.getEmail(),
				dto.getHomeLat(), dto.getHomeLong());
		return new ServerResponse(ServerResult.OK, player.getSid(),
				player.getId());
	}

	@Override
	public Player createPlayer(String username, String email, double homeLat,
			double homeLong) {
		homeLat = CalculationUtil.normalizeLatitute(homeLat);
		homeLong = CalculationUtil.normalizeLongitude(homeLong);
		Province home = provRepo.findWithLatLong(homeLat, homeLong);
		if (home == null) {
			home = new Province(homeLat, homeLong);
			provRepo.save(home);
		}
		String sid = GeneratorUtil.generateString(Constants.PLAYER_SID_LENGTH);

		Player player = new Player(username, email, sid, home);

		// Adding start units
		Unit startUnit = new Unit(Constants.PLAYER_START_UNITS);
		unitRepo.save(startUnit);
		player.getHome().addUnit(startUnit);

		homeRepo.save(player.getHome());
		playerRepo.save(player);
		return player;
	}

}
