package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.service.MovementService;

@Service
public class MovementServiceImpl implements MovementService {

	@Autowired
	PlayerRepository playerRepo;

	@Override
	public List<MovementSelectionViewDTO> getMyUnits(String cookie) {
		Player player = playerRepo.findBySid(cookie);
		List<MovementSelectionViewDTO> movements = new ArrayList<>();
		for (Ownership o : player.getOwnedProvinces()) {
			for (Unit unit : o.getUnits()) {
				movements.add(new MovementSelectionViewDTO(o.getProvince()
						.getName(), unit.getId(), unit.getSize()));
			}
		}

		for (Unit unit : player.getHome().getUnits()) {
			movements.add(new MovementSelectionViewDTO(player.getHome()
					.getProvince().getName(), unit.getId(), unit.getSize(),
					ProvinceType.HOME));
		}
		return movements;
	}

	@Override
	public BeginMovementResponse moveUnitsTo(String lat, String lon,
			List<BeginMovementDTO> beginMoveList, String cookie) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MovementViewDTO> getMyMovements(String cookie) {
		// TODO Auto-generated method stub
		return null;
	}

}
