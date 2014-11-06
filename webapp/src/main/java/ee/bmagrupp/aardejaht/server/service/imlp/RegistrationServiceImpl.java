package ee.bmagrupp.aardejaht.server.service.imlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.core.repository.UnitRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationResponse;
import ee.bmagrupp.aardejaht.server.service.RegistrationService;
import ee.bmagrupp.aardejaht.server.util.Constants;
import ee.bmagrupp.aardejaht.server.util.GeneratorUtil;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

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
	public RegistrationResponse registrationPhase1(RegistrationDTO dto) {
		Player player = playerRepo.findByUserName(dto.getUserName());
		if (player == null) {
			return new RegistrationResponse(ServerResult.OK);
		} else {
			return new RegistrationResponse(ServerResult.USERNAME_IN_USE);
		}

	}

	@Override
	public RegistrationResponse registrationPhase2(RegistrationDTO dto) {
		Player player = playerRepo.findByUserName(dto.getUserName());
		if (player != null) {
			return new RegistrationResponse(ServerResult.USERNAME_IN_USE);
		}
		player = createPlayer(dto.getUserName(), dto.getEmail(),
				dto.getHomeLat(), dto.getHomeLong());
		return new RegistrationResponse(ServerResult.OK, player.getSid(),
				player.getId());
	}

	@Override
	public Player createPlayer(String username, String email, double homeLat,
			double homeLong) {
		homeLat = normalizeLat(homeLat);
		homeLong = normalizeLong(homeLong);
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

	private double normalizeLong(double homeLong) {
		double rtrn = Math.floor(homeLong * 1000.0) / 1000.0;
		if ((rtrn * 1000.0) % 2 != 0) {
			rtrn = ((rtrn * 1000) - 1) / 1000.0;
		}
		rtrn += (Constants.PROVINCE_WIDTH / 2);
		return rtrn;
	}

	private double normalizeLat(double homeLat) {
		double rtrn = Math.floor(homeLat * 1000.0) / 1000.0;
		rtrn += (Constants.PROVINCE_HEIGHT / 2);
		return rtrn;
	}

}
