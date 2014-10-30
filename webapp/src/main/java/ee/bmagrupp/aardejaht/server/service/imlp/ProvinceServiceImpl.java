package ee.bmagrupp.aardejaht.server.service.imlp;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.aardejaht.server.core.domain.Ownership;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.domain.UnitState;
import ee.bmagrupp.aardejaht.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.OwnershipRepository;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.core.repository.UnitRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;
import static ee.bmagrupp.aardejaht.server.util.Constants.*;
import ee.bmagrupp.aardejaht.server.util.NameGenerator;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	private static Logger LOG = LoggerFactory
			.getLogger(ProvinceServiceImpl.class);
	private static Random rand = new Random();

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
	 * @param cookie
	 * @return returns the overall strength of a player
	 */

	private int findPlayerStrength(String cookie) {
		Player curPlayer = playerRepo.findBySid(cookie);
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
	 * @param lat2
	 *            /long2
	 * @param lat1
	 *            /long1
	 * @return The amount of columns/rows needed to be calculated
	 */

	private int calculateRowsNr(double lat1, double lat2) {
		lat1 = Math.floor(lat1 * 1000) / 1000;
		lat2 = Math.ceil(lat2 * 1000) / 1000;
		return (int) ((lat2 - lat1) / PROVINCE_HEIGHT);
	}

	private int calculateColumnNr(double long1, double long2) {
		long1 = Math.floor(long1 * 1000) / 1000;
		long2 = Math.ceil(long2 * 1000) / 1000;
		return (int) ((long2 - long1) / PROVINCE_WIDTH);
	}

	/**
	 * @author Sander
	 * @param a
	 *            - Ownership object
	 * @return all available units
	 */

	private int getProvinceStrength(Ownership a) {
		Set<Unit> units = a.getUnits();
		int overall = 0;
		for (Unit unit : units) {
			if (unit.getState() == UnitState.CLAIMED) {
				overall += unit.getSize();
			}
		}
		return overall;
	}

	/**
	 * @author Sander
	 * @param fov
	 *            - camera object
	 * @param playerId
	 *            - The player who sent the request
	 * @return returns an array with Province objects with info for the mobile
	 *         app
	 */

	@Override
	public List<ProvinceDTO> getProvinces(CameraFOV fov, String cookie) {
		ArrayList<ProvinceDTO> rtrn = new ArrayList<ProvinceDTO>();

		int columns = calculateColumnNr(fov.getSWlongitude(),
				fov.getNElongitude());
		int rows = calculateRowsNr(fov.getSWlatitude(), fov.getNElatitude());
		int playerStrength = findPlayerStrength(cookie);
		
		double baseLat = Math.floor(fov.getSWlatitude() * 1000.0) / 1000.0;
		double baseLong = Math.floor(fov.getSWlongitude() * 1000.0) / 1000.0;
		if ((baseLong * 1000.0) % 2 != 0) {
			baseLong = ((baseLong * 1000) - 1) / 1000.0;
		}

		List<Ownership> lst = (List<Ownership>) ownerRepo.findBetween(
				fov.getSWlatitude(), fov.getSWlongitude(), fov.getNElatitude(),
				fov.getNElongitude());

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				boolean found = false;
				Ownership foundArea = null;
				for (Ownership a : lst) {
					double x = a.getProvince().getLongitude();
					double y = a.getProvince().getLatitude();
					// Flow control for determining whether the ownership is
					// inside The marked area or not
					if (x > (baseLong + (j * PROVINCE_WIDTH))
							&& x < (baseLong + ((j + 1) * PROVINCE_WIDTH))
							&& y > (baseLat + (i * PROVINCE_HEIGHT))
							&& y < (baseLat + ((i + 1) * PROVINCE_HEIGHT))) {
						// -----
						Province temp = a.getProvince();
						int provinceStrength = getProvinceStrength(a);
						int playerId = playerRepo.findOwner(temp.getId())
								.getId();
						// -----
						rtrn.add(new ProvinceDTO(temp.getId(), temp
								.getLatitude(), temp.getLongitude(),
								provinceStrength, playerId, temp.getName()));
						found = true;
						foundArea = a;
						break;
					}
				}
				if (!found) {
					rtrn.add(generateProvince(baseLat + (i * PROVINCE_HEIGHT)
							+ (PROVINCE_HEIGHT / 2), baseLong
							+ (j * PROVINCE_WIDTH) + (PROVINCE_WIDTH / 2),
							playerStrength));
				} else {
					lst.remove(foundArea);
				}
			}
		}
		return rtrn;
	}

	private ProvinceDTO generateProvince(double latitude, double longitude,
			int playerStrength) {
		int min = playerStrength
				- (int) (playerStrength * BOT_STRENGTH_CONSTANT);
		int max = playerStrength
				+ (int) (playerStrength * BOT_STRENGTH_CONSTANT);
		int botStrength = rand.nextInt((max - min) + 1) + min;
		int provinceID = rand.nextInt((10000000 - 100000) + 1) + 100000;
		String name = NameGenerator.generate(PROVINCE_NAME_LENGTH);

		return new ProvinceDTO(provinceID, latitude, longitude, botStrength,
				BOT_ID, name);
	}

}
