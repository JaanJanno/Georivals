package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static ee.bmagrupp.georivals.server.game.util.Constants.*;
import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.domain.UnitState;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.game.GameLogic;
import ee.bmagrupp.georivals.server.game.PlayerService;
import ee.bmagrupp.georivals.server.game.util.CalculationUtil;
import ee.bmagrupp.georivals.server.game.util.GeneratorUtil;
import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.rest.domain.ServerResult;
import ee.bmagrupp.georivals.server.service.ProvinceService;

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

	@Autowired
	private PlayerService playerServ;

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
		ArrayList<ProvinceDTO> list = new ArrayList<ProvinceDTO>();
		Player player = playerRepo.findBySid(cookie);
		if (player == null) {
			return list;
		}
		Set<Ownership> provinces = player.getOwnedProvinces();
		HomeOwnership home = player.getHome();
		if (home != null) {
			Province homeProvince = home.getProvince();
			list.add(createHomeProvince(homeProvince, player));
		}
		if (provinces == null) {
			return list;
		}
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
		if (newHome == null) {
			newHome = new Province(lat, long1);
			provRepo.save(newHome);
		} else {
			Player tempPlayer = playerRepo.findOwnerOfProvince(newHome.getId());
			if (tempPlayer != null && tempPlayer.getId() == player.getId()) {
				Ownership temp = ownerRepo.findByProvinceId(newHome.getId());
				if (temp != null) {
					home = mergeUnits(home, temp);
					// Remove ownership from player
					player.getOwnedProvinces().remove(temp);
					ownerRepo.delete(temp);
				}
			}
		}
		home.setProvince(newHome);
		homeRepo.save(home);

		ServerResponse resp = new ServerResponse(ServerResult.OK);
		return resp;
	}

	private HomeOwnership mergeUnits(HomeOwnership home, Ownership temp) {
		for (Unit u : home.getUnits()) {
			if (u.getState() == UnitState.CLAIMED) {
				u.increaseSize(temp.countUnits());
				if (u.getSize() > 100) {
					u.setSize(100);
				}
			}
		}
		return home;
	}

	@Override
	public ServerResponse renameProvince(String latitude, String longitude,
			String newName, String cookie) {
		double lat = Double.parseDouble(latitude);
		double long1 = Double.parseDouble(longitude);
		lat = CalculationUtil.normalizeLatitute(lat);
		long1 = CalculationUtil.normalizeLongitude(long1);
		Province prov = provRepo.findWithLatLong(lat, long1);
		if (prov == null) {
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
	 * @author TKasekamp
	 * @param fov
	 *            - camera object
	 * @param playerId
	 *            - The player who sent the request
	 * @return returns an array with Province objects with info for the mobile
	 *         app
	 */

	@Override
	public List<ProvinceDTO> getProvinces(CameraFOV fov, String cookie) {
		// calculate the amount of rows and columns
		int columns = calculateColumnNr(fov.getSwlongitude(),
				fov.getNelongitude());
		int rows = calculateRowsNr(fov.getSwlatitude(), fov.getNelatitude());

		// calculate base coordinates on which to run the FOR loops
		double baseLat = Math.floor(fov.getSwlatitude() * 1000.0) / 1000.0;
		double baseLong = findBaseLong(fov.getSwlongitude());

		// Player stuff calculation
		Player player = playerRepo.findBySid(cookie);
		int playerStrength = findPlayerStrength(player);
		HomeOwnership home = getHome(player);
		boolean checkForHome = false;
		if (home != null) {
			checkForHome = true;
		}

		// All ownerships in this area
		List<Ownership> owList = (List<Ownership>) ownerRepo.findBetween(
				roundLatitude(fov.getSwlatitude(), -1),
				roundLongitude(fov.getSwlongitude(), -1),
				roundLatitude(fov.getNelatitude(), 1),
				roundLongitude(fov.getNelongitude(), 1));

		List<ProvinceDTO> dtos = new ArrayList<>();

		double latitude;
		double longitude;
		Date date = new Date();

		// Looping through rows and columns
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				boolean found = false;
				latitude = baseLat + (i * PROVINCE_HEIGHT)
						+ (PROVINCE_HEIGHT / 2);
				longitude = baseLong + (j * PROVINCE_WIDTH)
						+ (PROVINCE_WIDTH / 2);

				if (checkForHome && homeCheck(home, latitude, longitude)) {
					dtos.add(createHomeProvince(home.getProvince(), player));
					checkForHome = false;
					continue;

				}
				// Removing elements while iterating
				for (Iterator<Ownership> iterator = owList.iterator(); iterator
						.hasNext();) {
					Ownership ownership = iterator.next();
					double lat = ownership.getProvince().getLatitude();
					double lon = ownership.getProvince().getLongitude();
					// -- Is ownership is within limits
					if (lon > (baseLong + (j * PROVINCE_WIDTH))
							&& lon < (baseLong + ((j + 1) * PROVINCE_WIDTH))
							&& lat > (baseLat + (i * PROVINCE_HEIGHT))
							&& lat < (baseLat + ((i + 1) * PROVINCE_HEIGHT))) {
						dtos.add(createExistingProvince(ownership, player,
								playerStrength, date));
						found = true;
						iterator.remove();
						break;

					}
				}

				if (!found) {
					dtos.add(createBOTProvince(latitude, longitude,
							playerStrength));
				}

			}
		}
		return dtos;

	}

	private HomeOwnership getHome(Player requestMaker) {
		if (requestMaker == null) {
			return null;
		}
		return requestMaker.getHome();
	}

	private double findBaseLong(double swlongitude) {
		double baseLong = Math.floor(swlongitude * 1000.0) / 1000.0;
		if ((baseLong * 1000.0) % 2 != 0) {
			baseLong = ((baseLong * 1000) - 1) / 1000.0;
		}
		return baseLong;
	}

	/**
	 * @author Sander
	 * @param player
	 * @return Calculate player total strength
	 */

	public int findPlayerStrength(Player player) {
		if (player == null) {
			return PLAYER_DEFAULT_STRENGTH;
		}
		return playerServ.calculatePlayerStrength(player);
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
		lat1 = roundLatitude(lat1, -1);
		lat2 = roundLatitude(lat2, 1);
		return (int) Math.round(((lat2 - lat1) / PROVINCE_HEIGHT));
	}

	private int calculateColumnNr(double long1, double long2) {
		long1 = roundLongitude(long1, -1);
		long2 = roundLongitude(long2, 1);
		return (int) Math.round(((long2 - long1) / PROVINCE_WIDTH));
	}

	private double roundLatitude(double lat, int way) {
		if (way == 1) {
			lat = Math.ceil(lat * 1000) / 1000;
		} else {
			lat = Math.floor(lat * 1000) / 1000;
		}
		return lat;
	}

	private double roundLongitude(double long1, int way) {
		if (way == 1) {
			long1 = Math.ceil(long1 * 1000) / 1000;
			if ((long1 * 1000.0) % 2 != 0) {
				long1 = ((long1 * 1000) + 1) / 1000.0;
			}
		} else {
			long1 = Math.floor(long1 * 1000) / 1000;
			if ((long1 * 1000.0) % 2 != 0) {
				long1 = ((long1 * 1000) - 1) / 1000.0;
			}
		}
		return long1;

	}

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
		int unitCount = GameLogic.botUnits(lat, lon);
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
		return createExistingProvince(ow, player, playerStrength, new Date());
	}

	/**
	 * Creates a {@link ProvinceDTO} for a province that exists in the database.
	 * * Else it's owned by any player. UnderAttack check required. New units
	 * needed if province owned by the player. isAttackable check required if
	 * province owned by another player.
	 * 
	 * @param ow
	 *            {@link Ownership} of {@link Province}
	 * @param player
	 *            {@link Player} making the request
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @param date
	 *            current {@link Date}
	 * @return {@link ProvinceDTO}
	 */
	private ProvinceDTO createExistingProvince(Ownership ow, Player player,
			int playerStrength, Date date) {
		Province prov = ow.getProvince();

		Player player2 = playerRepo.findOwnerOfProvince(ow.getProvince()
				.getId());
		int unitSize = ow.countUnits();

		ProvinceType type;
		boolean isAttackable = false;
		int newUnits = 0;
		boolean underAttack = false;
		if ((player != null) && (player.getId() == player2.getId())) {
			// Province owned by the player
			type = ProvinceType.PLAYER;
			newUnits = GameLogic.generateNewUnits(ow.getLastVisit(), date,
					unitSize);
			underAttack = movementRepo.checkIfUnderAttack(player.getId(),
					prov.getId());

		} else if (player2.getId() == BOT_ID) {
			type = ProvinceType.BOT;
			isAttackable = true;
		} else {
			// Province owned by somebody else
			type = ProvinceType.OTHER_PLAYER;
			// If no player then all provinces attackable
			if (player != null) {
				int player2Strength = findPlayerStrength(player2);
				isAttackable = GameLogic.canAttack(playerStrength,
						player2Strength);
			} else {
				isAttackable = true;
			}
		}

		return new ProvinceDTO(prov.getLatitude(), prov.getLongitude(), type,
				ow.getProvinceName(), player2.getUserName(), isAttackable,
				underAttack, unitSize, newUnits);
	}

	/**
	 * Check if the {@link HomeOwnership} {@link Province} is defined by these
	 * parameters
	 * 
	 * @param home
	 *            {@link HomeOwnership}
	 * @param latitude
	 * @param longitude
	 * @return true if these are the home's coordinates
	 */
	private boolean homeCheck(HomeOwnership home, double latitude,
			double longitude) {
		boolean latBetween = (home.getProvince().getLatitude() > latitude - 0.00001)
				&& (home.getProvince().getLatitude() < latitude + 0.00001);
		boolean lonBetween = (home.getProvince().getLongitude() > longitude - 0.00001)
				&& (home.getProvince().getLongitude() < longitude + 0.00001);
		return latBetween && lonBetween;
	}

}
