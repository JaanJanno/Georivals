package ee.bmagrupp.georivals.server.service.imlp;

import java.util.List;

import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.service.MovementService;

@Service
public class MovementServiceImpl implements MovementService {

	@Override
	public List<MovementSelectionViewDTO> getMyUnits(String cookie) {
		// TODO Auto-generated method stub
		return null;
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
