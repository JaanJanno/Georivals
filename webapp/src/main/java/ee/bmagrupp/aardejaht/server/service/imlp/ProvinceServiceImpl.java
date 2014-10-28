package ee.bmagrupp.aardejaht.server.service.imlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Ownership;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.domain.UnitState;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.OwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.core.repository.UnitRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.Province;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	private static Logger LOG = LoggerFactory
			.getLogger(ProvinceServiceImpl.class);

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

	/**
	 * @author Sander
	 * @param playerId
	 * @return returns the overall strength of a player
	 */

	private int findPlayerStrength(int playerId) {
		Player curPlayer = playerRepo.findOne(playerId);
		Set<Ownership> provinces = curPlayer.getOwnedProvinces();
		int overall = 0;
		for (Ownership b : provinces) {
			Set<Unit> units = b.getUnits();
			for (Unit unit : units) {
				if (unit.getState() == UnitState.CLAIMED) {
					overall += unit.getSize();
				}
			}
		}
		return overall;
	}

	/**
	 * @author Sander
	 * @param latitude
	 * @param longitude
	 * @param playerId
	 *            - The player who sent the request
	 * @return returns an array with Province objects with info for the mobile
	 *         app
	 */

	public List<Province> getProvinces(double lat1, double lat2, double long1,
			double long2, int playerId) {
		ArrayList<ee.bmagrupp.aardejaht.server.rest.domain.Province> rtrn = new ArrayList<ee.bmagrupp.aardejaht.server.rest.domain.Province>();
		lat1 = Math.round(lat1 * 1000) / 1000;
		lat2 = Math.round(lat2 * 1000) / 1000;
		long1 = Math.round(long1 * 1000) / 1000;
		long2 = Math.round(long2 * 1000) / 1000;
		int playerStrength = findPlayerStrength(playerId);

		List<Ownership> lst = ownerRepo.findBetween(long1, lat1, long2, lat2);
		for (Ownership a : lst) {
			Player player = playerRepo.findOwner(a.getId());
			if (player.getId() == 0) {
				rtrn.add(new ee.bmagrupp.aardejaht.server.rest.domain.Province(
						a.getId(), a.getProvince().getLatitude(), a
								.getProvince().getLongitude(), playerStrength,
						player.getId(), a.getProvince().getName()));
			} else {
				int overall = 0;
				Set<Unit> units = a.getUnits();
				for (Unit b : units) {
					if (b.getState() == UnitState.CLAIMED) {
						overall += b.getSize();
					}
				}
				rtrn.add(new ee.bmagrupp.aardejaht.server.rest.domain.Province(
						a.getId(), a.getProvince().getLatitude(), a
								.getProvince().getLongitude(), overall, player
								.getId(), a.getProvince().getName()));
			}
		}
		return rtrn;
	}

	@Override
	public List<Province> getProvinces(CameraFOV fov, String cookie) {
		// TODO Sander, put your magic code here
		return null;
	}

}
