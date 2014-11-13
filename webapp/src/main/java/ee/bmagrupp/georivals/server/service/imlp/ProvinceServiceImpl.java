package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
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
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.domain.UnitState;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.ProvinceService;
import ee.bmagrupp.georivals.server.util.Constants;
import ee.bmagrupp.georivals.server.util.GeneratorUtil;

@Service
public class ProvinceServiceImpl implements ProvinceService {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(ProvinceServiceImpl.class);
	private Random rand = new Random();

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

	@Autowired
	private MovementRepository movementRepo;

	/**
	 * This is a delegating method. The real action is in
	 * {@link #getProvince(double, double, Player, int)}.
	 * 
	 * @author TKasekamp
	 */
	@Override
	public ProvinceViewDTO getProvince(String latitude, String longitude,
			String cookie) {
		double lat = Double.parseDouble(latitude);
		double lon = Double.parseDouble(longitude);

		Player player = playerRepo.findBySid(cookie);

		int playerStrength = findPlayerStrength(player);
		return getProvince(lat, lon, player, playerStrength);

	}

	@Override
	public List<ProvinceViewDTO> getMyProvinces(String cookie) {
		Player player = playerRepo.findBySid(cookie);
		Set<Ownership> provinces = player.getOwnedProvinces();
		ArrayList<ProvinceViewDTO> list = new ArrayList<ProvinceViewDTO>();
		HomeOwnership home = player.getHome();
		Province homeProvince = home.getProvince();
		list.add(new ProvinceViewDTO(homeProvince.getLatitude(), homeProvince.getLongitude(),
				homeProvince.getName(), ProvinceType.HOME,
				player.getUserName(), countUnits(home.getUnits())));
		for (Ownership a : provinces){
			Province temp = a.getProvince();
			list.add(new ProvinceViewDTO(temp.getLatitude(),temp.getLongitude(),temp.getName(),ProvinceType.PLAYER,player.getUserName(),countUnits(a.getUnits())));
		}
		return list;
	}

	@Override
	public ServerResponse changeHomeProvince(String latitude, String longitude,
			String cookie) {
		return null;
	}

	@Override
	public ServerResponse renameProvince(String latitude, String longitude,
			String newName, String cookie) {
		// TODO Auto-generated method stub
		return null;
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

		int columns = calculateColumnNr(fov.getSwlongitude(),
				fov.getNelongitude());

		int rows = calculateRowsNr(fov.getSwlatitude(), fov.getNelatitude());
		Player tempPlayer = playerRepo.findBySid(cookie);
		int playerStrength = PLAYER_DEFAULT_STRENGTH;
		int curPlayerId = PLAYER_DEFAULT_ID;
		if (tempPlayer != null) {
			curPlayerId = tempPlayer.getId();
			playerStrength = findPlayerStrength(tempPlayer);
		}

		double baseLat = Math.floor(fov.getSwlatitude() * 1000.0) / 1000.0;
		double baseLong = Math.floor(fov.getSwlongitude() * 1000.0) / 1000.0;
		if ((baseLong * 1000.0) % 2 != 0) {
			baseLong = ((baseLong * 1000) - 1) / 1000.0;
		}

		List<Ownership> lst = (List<Ownership>) ownerRepo.findBetween(
				fov.getSwlatitude(), fov.getSwlongitude(), fov.getNelatitude(),
				fov.getNelongitude());

		// For generating new units
		Date currentDate = new Date();
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
						int newUnits = 0;
						if (curPlayerId == playerId) {
							newUnits = generateNewUnits(a.getLastVisit(),
									currentDate, provinceStrength);
						}
						// -----
						rtrn.add(new ProvinceDTO(temp.getId(), temp
								.getLatitude(), temp.getLongitude(),
								provinceStrength, playerId, temp.getName(),
								newUnits));
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
		if (botStrength > 100) {
			botStrength = 100;
		} else if (botStrength < 1) {
			botStrength = 1;
		}
		int provinceID = rand.nextInt((10000000 - 100000) + 1) + 100000;
		String name = GeneratorUtil.generateString(PROVINCE_NAME_LENGTH);

		return new ProvinceDTO(provinceID, latitude, longitude, botStrength,
				BOT_ID, name, 0);
	}

	/**
	 * Returns the number of new Units to be added to this Province. New units
	 * are added if: <br>
	 * 1) The difference between currentDate and lastVisitDate is more than
	 * {@link Constants#UNIT_GENERATION_TIME}<br>
	 * 2) The provinceStrenght is less than {@link Constants#PROVINCE_UNIT_MAX}<br>
	 * If both 1 and 2 are true then an int between
	 * {@link Constants#UNIT_GENERATION_MIN} and
	 * {@link Constants#UNIT_GENERATION_MAX} is returned. If this int +
	 * provinceStrenght now exceeds {@link Constants#PROVINCE_UNIT_MAX}, the
	 * return int will be decreased so that there are a total of
	 * {@link Constants#PROVINCE_UNIT_MAX} Units in this province. <br>
	 * Otherwise 0 will be returned.
	 * 
	 * @param lastVisitDate
	 *            The last visit {@link Date} from {@link Ownership}
	 * @param currentDate
	 *            The current {@link Date}
	 * @param provinceStrength
	 *            The number of units in this province
	 * @author TKasekamp
	 * @return Integer
	 */
	private int generateNewUnits(Date lastVisitDate, Date currentDate,
			int provinceStrength) {
		long a = currentDate.getTime() - lastVisitDate.getTime();
		if ((a > UNIT_GENERATION_TIME)
				&& (provinceStrength < PROVINCE_UNIT_MAX)) {
			int b = GeneratorUtil.generateWithSeed(lastVisitDate);
			if (b + provinceStrength > PROVINCE_UNIT_MAX) {
				b = PROVINCE_UNIT_MAX - provinceStrength;
			}
			return b;
		}
		return 0;
	}

	/**
	 * @author Sander
	 * @param cookie
	 * @return returns the overall strength of a player
	 */

	private int findPlayerStrength(String cookie) {
		Player player = playerRepo.findBySid(cookie);
		return findPlayerStrength(player);
	}

	/**
	 * Returns the total number of units for this player. If player is null,
	 * returns {@link Constants#PLAYER_DEFAULT_STRENGTH}
	 * 
	 * @author Sander
	 * @author TKasekamp
	 * @param Player
	 *            {@link Player}
	 * @return returns the overall strength of a player
	 */

	private int findPlayerStrength(Player player) {
		if (player == null) {
			return PLAYER_DEFAULT_STRENGTH;
		}
		Set<Ownership> provinces = player.getOwnedProvinces();
		int overall = 0;
		for (Ownership b : provinces) {
			overall += countUnits(b.getUnits());
		}
		overall += countUnits(player.getHome().getUnits());
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

	/**
	 * @author Sander
	 * @param a
	 *            - Ownership object
	 * @return all available units
	 */

	private int getProvinceStrength(Ownership a) {
		return countUnits(a.getUnits());
	}

	/**
	 * Returns the sum of the unit sizes.
	 * 
	 * @author TKasekamp
	 * @param units
	 * @return
	 */
	private int countUnits(Set<Unit> units) {
		int overall = 0;
		for (Unit unit : units) {
			if (unit.getState() == UnitState.CLAIMED) {
				overall += unit.getSize();
			}
		}
		return overall;
	}

	/**
	 * Checks if the player defined by player1Strength can attack the provinces
	 * owned by player2Strength. Returns true if player1 is not over 2 times
	 * bigger then player2.
	 * 
	 * @param player1Strength
	 *            Current player
	 * @param player2Strength
	 *            Player to check against
	 * @author TKasekamp
	 * @return {@code boolean}
	 */
	private boolean canAttack(int player1Strength, int player2Strength) {
		if (player2Strength * 2 >= player1Strength) {
			return true;
		}
		return false;
	}

	// ProvinceViewDTO creation

	/**
	 * Returns a {@link ProvinceViewDTO} defined by the coordinates.
	 * 
	 * @param latitude
	 * @param longitude
	 * @param player
	 *            {@link Player} making the request
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @author TKasekamp
	 * @return {@link ProvinceViewDTO}
	 */
	private ProvinceViewDTO getProvince(double latitude, double longitude,
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
	 * Creates a {@link ProvinceViewDTO} for a BOT owned province at this
	 * location. New units not needed. Not under attack. Attackable not needed
	 * 
	 * @param lat
	 *            latitude
	 * @param lon
	 *            longitude
	 * @param playerStrength
	 *            {@link Player}'s total number of units
	 * @author TKasekamp
	 * @return {@link ProvinceViewDTO} with default values for a BOT owned
	 *         province.
	 */
	private ProvinceViewDTO createBOTProvince(double lat, double lon,
			int playerStrength) {
		String provName = GeneratorUtil.generateString(PROVINCE_NAME_LENGTH,
				lat, lon);
		int unitCount = GeneratorUtil.botUnits(lat, lon, playerStrength);
		return new ProvinceViewDTO(lat, lon, provName, unitCount);
	}

	/**
	 * Creates a {@link ProvinceViewDTO} for the players home province. Cannot
	 * be under attack. New unit check necessary. Owner name is known.
	 * 
	 * @param prov
	 *            {@link Province}
	 * @param player
	 *            {@link Player} making the request
	 * @author TKasekamp
	 * @return {@link ProvinceViewDTO} with default values for a home province.
	 */
	private ProvinceViewDTO createHomeProvince(Province prov, Player player) {
		int unitSize = countUnits(player.getHome().getUnits());
		int newUnits = generateNewUnits(player.getHome().getLastVisit(),
				new Date(), unitSize);
		return new ProvinceViewDTO(prov.getLatitude(), prov.getLongitude(),
				player.getUserName(), prov.getName(), unitSize, newUnits);
	}

	/**
	 * Creates a {@link ProvinceViewDTO} for a province that exists in the
	 * database. If it only exists as a home province a BOT province will be
	 * created. <br>
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
	 * @return {@link ProvinceViewDTO}
	 */
	private ProvinceViewDTO createExistingProvince(Province prov,
			Player player, int playerStrength) {
		Ownership ow = ownerRepo.findByProvinceId(prov.getId());
		if (ow == null) {
			// Province is only a home province
			return createBOTProvince(prov.getLatitude(), prov.getLongitude(),
					playerStrength);
		}

		Player player2 = playerRepo.findOwnerOfProvince(prov.getId());
		int unitSize = countUnits(ow.getUnits());
		ProvinceType type;
		boolean isAttackable = false;
		int newUnits = 0;
		if ((player != null) && (player.getId() == player2.getId())) {
			// Province owned by the player
			type = ProvinceType.PLAYER;
			newUnits = generateNewUnits(ow.getLastVisit(), new Date(), unitSize);

		} else {
			// Province owned by somebody else
			type = ProvinceType.OTHER_PLAYER;
			int player2Strength = findPlayerStrength(player2);
			isAttackable = canAttack(playerStrength, player2Strength);
		}

		boolean underAttack = movementRepo.checkIfDestination(prov.getId());
		return new ProvinceViewDTO(prov.getLatitude(), prov.getLongitude(),
				type, prov.getName(), player2.getUserName(), isAttackable,
				underAttack, unitSize, newUnits);
	}

}
