package ee.bmagrupp.aardejaht.server.service.imlp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationResponse;
import ee.bmagrupp.aardejaht.server.service.AuthenticationService;
import ee.bmagrupp.aardejaht.server.util.Constants;
import ee.bmagrupp.aardejaht.server.util.NameGenerator;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Autowired
	ProvinceRepository provRepo;

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
		player = savePlayer(dto);
		return new RegistrationResponse(ServerResult.OK, player.getSid(),
				player.getId());
	}

	/**
	 * Inserts a new {@link Player} into the database. To do this, the home
	 * province and home ownership must be persisted. The province repository is
	 * checked if this province is already present.
	 * 
	 * @param dto
	 *            {@link RegistrationDTO}
	 * @return {@link Player} with ID and SID
	 */
	private Player savePlayer(RegistrationDTO dto) {
		Province home = provRepo.findWithLatLong(dto.getHome_lat(),
				dto.getHome_long());
		if (home == null) {
			home = new Province(dto.getHome_long(), dto.getHome_lat());
			provRepo.save(home);
		}
		String sid = NameGenerator.generate(Constants.PLAYER_SID_LENGTH);

		Player player = new Player(dto.getUserName(), dto.getEmail(), sid, home);
		// TODO set player starting units

		homeRepo.save(player.getHome());
		playerRepo.save(player);
		return player;
	}

}
