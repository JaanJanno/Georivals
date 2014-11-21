package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ee.bmagrupp.georivals.server.util.Constants.*;
import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.game.GameLogic;
import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.ProvinceService;
import ee.bmagrupp.georivals.server.util.CalculationUtil;
import ee.bmagrupp.georivals.server.util.GeneratorUtil;
import ee.bmagrupp.georivals.server.util.ServerResult;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
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
	private MovementRepository movementRepo;

	/**
	 * This is a delegating method. The real action is in
	 * {@link #getProvince(double, double, Player, int)}.
	 * 
	 * @author TKasekamp
	 */
	@Override
	public ProvinceDTO getProvince(String latitude, String longitude,
			String cookie) {
		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);

		lat = CalculationUtil.normalizeLatitute(lat);
		lon = CalculationUtil.normalizeLongitude(lon);
		Player player = playerRepo.findBySid(cookie);

		int playerStrength = findPlayerStrength(player);
		return getProvince(lat, lon, player, playerStrength);

	}

	@Override
	public List<ProvinceDTO> getMyProvinces(String cookie) {
		Player player = playerRepo.findBySid(cookie);
		Set<Ownership> provinces = player.getOwnedProvinces();
		ArrayList<ProvinceDTO> list = new ArrayList<ProvinceDTO>();
		HomeOwnership home = player.getHome();
		Province homeProvince = home.getProvince();
		list.add(createHomeProvince(homeProvince, player));
		for (Ownership a : provinces) {
			Province temp = a.getProvince();
			boolean underAttack = movementRepo.checkIfDestination(temp.getId());
			int unitSize = a.countUnits();
			int newUnits = GameLogic.generateNewUnits(a.getLastVisit(),
					new Date(), unitSize);
			list.add(new ProvinceDTO(temp.getLatitude(), temp.getLongitude(),
					ProvinceType.PLAYER, a.getProvinceName(), player
							.getUserName(), false, underAttack, unitSize,
					newUnits));
		}
		return list;
	}

	@Override
	public ServerResponse changeHomeProvince(String latitude, String longitude,
			String cookie) {
		Player player = playerRepo.findBySid(cookie);
		HomeOwnership home = player.getHome();
		double lat = Double.parseDouble(latitude);
		double long1 = Double.parseDouble(longitude);
		lat = CalculationUtil.normalizeLatitute(lat);
		long1 = CalculationUtil.normalizeLongitude(long1);
		Province newHome = null;
		newHome = provRepo.findWithLatLong(lat, long1);
		if(newHome == null){
			newHome = new Province(lat, long1);
			provRepo.save(newHome);
		}
		home.setProvince(newHome);
		home.setProvinceName(GeneratorUtil.generateString(PROVINCE_NAME_LENGTH, lat, long1));
		homeRepo.save(home);
		
		ServerResponse resp = new ServerResponse(ServerResult.OK);
		return resp;
	}

	@Override
	public ServerResponse renameProvince(String latitude, String longitude,
			String newName, String cookie) {
		double lat = Double.parseDouble(latitude);
		double long1 = Double.parseDouble(longitude);
		lat = CalculationUtil.normalizeLatitute(lat);
		long1 = CalculationUtil.normalizeLongitude(long1);
		Province prov = provRepo.findWithLatLong(lat, long1);
		if(prov == null){
			ServerResponse resp = new ServerResponse(ServerResult.FAIL,
					"Not your province");
			return resp;
		}
		Player player = playerRepo.findBySid(cookie);
		Set<Ownership> lst = player.getOwnedProvinces();
		for (Ownership a : lst) {
			if (a.getProvince().equals(prov)) {
				a.setProvinceName(newName);
				ownerRepo.save(a);
				ServerResponse resp = new ServerResponse(ServerResult.OK);
				return resp;
			}
		}
		HomeOwnership home = player.getHome();
		if (home.getProvince().getLatitude() == prov.getLatitude()
				&& home.getProvince().getLongitude() == prov.getLongitude()) {
			home.setProvinceName(newName);
			homeRepo.save(home);
			ServerResponse resp = new ServerResponse(ServerResult.OK);
			return resp;
		}
		ServerResponse resp = new ServerResponse(ServerResult.FAIL,
				"Not your province");
		return resp;
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
		
		// calculate the amount of rows and columns
		int columns = calculateColumnNr(fov.getSwlongitude(),
				fov.getNelongitude());
		int rows = calculateRowsNr(fov.getSwlatitude(), fov.getNelatitude());
		// ------
		// Get player related stats from the system and set variables
		Player tempPlayer = playerRepo.findBySid(cookie);
		int playerStrength = PLAYER_DEFAULT_STRENGTH;
		int curPlayerId = PLAYER_DEFAULT_ID;
		if (tempPlayer != null) {
			curPlayerId = tempPlayer.getId();
			playerStrength = findPlayerStrength(tempPlayer);
		}
		// ------
		// calculate base coordinates on which to un the FOR loops
		double baseLat = Math.floor(fov.getSwlatitude() * 1000.0) / 1000.0;
		double baseLong = Math.floor(fov.getSwlongitude() * 1000.0) / 1000.0;
		if ((baseLong * 1000.0) % 2 != 0) {
			baseLong = ((baseLong * 1000) - 1) / 1000.0;
		}
		// -------
		// find all areas within the specified coordinates
		List<Ownership> lst = (List<Ownership>) ownerRepo.findBetween(
				fov.getSwlatitude(), fov.getSwlongitude(), fov.getNelatitude(),
				fov.getNelongitude());
		// -------
		// For generating new units
		Date currentDate = new Date();
		// -------
		// Find out if home province is within the boundaries
		boolean homeIncluded = false;
		HomeOwnership home = null;
		if(tempPlayer != null){
			home = tempPlayer.getHome();
			homeIncluded = HomeInArea(fov.getSwlatitude(),fov.getSwlongitude(),fov.getNelatitude(),fov.getNelongitude(),home);
		}
		// -------
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				boolean found = false;
				Ownership foundArea = null;
				//Find if this is home
				if(homeIncluded && home.getProvince().getLongitude() > (baseLong + (j * PROVINCE_WIDTH))
						&& home.getProvince().getLongitude() < (baseLong + ((j + 1) * PROVINCE_WIDTH))
						&& home.getProvince().getLatitude() > (baseLat + (i * PROVINCE_HEIGHT))
						&& home.getProvince().getLatitude() < (baseLat + ((i + 1) * PROVINCE_HEIGHT))){
					int newUnits = 0;
					newUnits = GameLogic.generateNewUnits(
							home.getLastVisit(), currentDate,
							home.countUnits());
					rtrn.add(new ProvinceDTO(home.getProvince().getLatitude(), home.getProvince()
							.getLongitude(), ProvinceType.HOME, home.getProvinceName()
							, tempPlayer.getUserName(), true, false, home.countUnits(), newUnits));
					continue;
				}
				else{
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
							int provinceStrength = a.countUnits();
							Player player = playerRepo.findOwnerOfProvince(temp
									.getId());
							int playerId = player.getId();
							int newUnits = 0;
							if (curPlayerId == playerId) {
								newUnits = GameLogic.generateNewUnits(
										a.getLastVisit(), currentDate,
										provinceStrength);
							}
							// -----
							if (curPlayerId == playerId) {
								rtrn.add(new ProvinceDTO(temp.getLatitude(), temp
										.getLongitude(), ProvinceType.PLAYER, a
										.getProvinceName(), player.getUserName(),
										true, false, a.countUnits(), newUnits));
							} else {
								rtrn.add(new ProvinceDTO(temp.getLatitude(), temp
										.getLongitude(), ProvinceType.OTHER_PLAYER,
										a.getProvinceName(), player.getUserName(),
										true, false, a.countUnits(), newUnits));
							}
							found = true;
							foundArea = a;
							break;
						}
					}
				}
				if (!found) {
					rtrn.add(createBOTProvince(baseLat + (i * PROVINCE_HEIGHT)
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

	private boolean HomeInArea(double swlatitude, double swlongitude,
			double nelatitude, double nelongitude, HomeOwnership home) {
		Province homeProv = home.getProvince();
		if(swlatitude < homeProv.getLatitude() && nelatitude > homeProv.getLatitude()
			&& swlongitude < homeProv.getLongitude() && nelongitude > homeProv.getLongitude()){
			return true;
		}
		return false;
	}

	public int findPlayerStrength(Player player) {
		if (player == null) {
			return PLAYER_DEFAULT_STRENGTH;
		}
		return player.findPlayerUnitCount();
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
		return (int) Math.round(((lat2 - lat1) / PROVINCE_HEIGHT));
	}

	private int calculateColumnNr(double long1, double long2) {
		long1 = Math.floor(long1 * 1000) / 1000;
		if ((long1 * 1000.0) % 2 != 0) {
			long1 = ((long1 * 1000) - 1) / 1000.0;
		}
		long2 = Math.ceil(long2 * 1000) / 1000;
		if ((long2 * 1000.0) % 2 != 0) {
			long2 = ((long2 * 1000) + 1) / 1000.0;
		}
		return (int) Math.round(((long2 - long1) / PROVINCE_WIDTH));
	}

	// ProvinceViewDTO creation

	/**
	 * Returns a {@link ProvinceDTO} defined by the coordinates.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param player
	 *            {@link Player} making the request
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @author TKasekamp
	 * @return {@link ProvinceDTO}
	 */
	private ProvinceDTO getProvince(double latitude, double longitude,
			Player player, int playerStrength) {
		Province prov = provRepo.findWithLatLong(latitude, longitude);

		if (prov == null) {
			// Not in database -> bot owned
			return createBOTProvince(latitude, longitude, playerStrength);
		} else if ((player != null)
				&& (prov.getId() == player.getHome().getProvince().getId())) {
			// In database and home province of player -> home province
			return createHomeProvince(prov, player);

		} else {
			// In database -> owned by any player or a home province of other
			// player
			return createExistingProvince(prov, player, playerStrength);

		}
	}

	/**
	 * Creates a {@link ProvinceDTO} for a BOT owned province at this location.
	 * New units not needed. Not under attack. Attackable not needed
	 * 
	 * @param lat
	 *            latitude
	 * @param lon
	 *            longitude
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @author TKasekamp
	 * @return {@link ProvinceDTO} with default values for a BOT owned province.
	 */
	private ProvinceDTO createBOTProvince(double lat, double lon,
			int playerStrength) {
		String provName = GeneratorUtil.generateString(PROVINCE_NAME_LENGTH,
				lat, lon);
		int unitCount = GameLogic.botUnits(lat, lon, playerStrength);
		return new ProvinceDTO(lat, lon, provName, unitCount);
	}

	/**
	 * Creates a {@link ProvinceDTO} for the players home province. Cannot be
	 * under attack. New unit check necessary. Owner name is known.
	 * 
	 * @param prov
	 *            {@link Province}
	 * @param player
	 *            {@link Player} making the request
	 * @author TKasekamp
	 * @return {@link ProvinceDTO} with default values for a home province.
	 */
	private ProvinceDTO createHomeProvince(Province prov, Player player) {
		int unitSize = player.getHome().countUnits();
		int newUnits = GameLogic.generateNewUnits(player.getHome()
				.getLastVisit(), new Date(), unitSize);
		return new ProvinceDTO(prov.getLatitude(), prov.getLongitude(),
				player.getUserName(), player.getHome().getProvinceName(),
				unitSize, newUnits);
	}

	/**
	 * Creates a {@link ProvinceDTO} for a province that exists in the database.
	 * If it only exists as a home province a BOT province will be created. <br>
	 * Else it's owned by any player. UnderAttack check required. New units
	 * needed if province owned by the player. isAttackable check required if
	 * province owned by another player.
	 * 
	 * @param prov
	 *            {@link Province}
	 * @param player
	 *            {@link Player} making the request
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @author TKasekamp
	 * @return {@link ProvinceDTO}
	 */
	private ProvinceDTO createExistingProvince(Province prov, Player player,
			int playerStrength) {
		Ownership ow = ownerRepo.findByProvinceId(prov.getId());
		if (ow == null) {
			// Province is only a home province
			return createBOTProvince(prov.getLatitude(), prov.getLongitude(),
					playerStrength);
		}

		Player player2 = playerRepo.findOwnerOfProvince(prov.getId());
		int unitSize = ow.countUnits();

		ProvinceType type;
		boolean isAttackable = false;
		int newUnits = 0;
		if ((player != null) && (player.getId() == player2.getId())) {
			// Province owned by the player
			type = ProvinceType.PLAYER;
			newUnits = GameLogic.generateNewUnits(ow.getLastVisit(),
					new Date(), unitSize);

		} else {
			// Province owned by somebody else
			type = ProvinceType.OTHER_PLAYER;
			int player2Strength = findPlayerStrength(player2);
			isAttackable = GameLogic.canAttack(playerStrength, player2Strength);
		}

		boolean underAttack = movementRepo.checkIfDestination(prov.getId());
		return new ProvinceDTO(prov.getLatitude(), prov.getLongitude(), type,
				ow.getProvinceName(), player2.getUserName(), isAttackable,
				underAttack, unitSize, newUnits);
	}

}
