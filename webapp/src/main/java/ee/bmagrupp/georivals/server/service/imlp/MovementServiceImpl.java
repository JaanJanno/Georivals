package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.game.GameLogic;
import ee.bmagrupp.georivals.server.game.MovementWorker;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.MovementService;
import ee.bmagrupp.georivals.server.util.CalculationUtil;
import ee.bmagrupp.georivals.server.util.ServerResult;
import static ee.bmagrupp.georivals.server.util.Constants.PROVINCE_UNIT_MIN;

/**
 * Implementation of movement stuff.
 * 
 * @author TKasekamp
 *
 */
@Service
public class MovementServiceImpl implements MovementService {

	private static final Logger LOG = LoggerFactory
			.getLogger(MovementServiceImpl.class);

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	MovementRepository movementRepo;

	@Autowired
	ProvinceRepository provRepo;

	@Autowired
	UnitRepository unitRepo;

	@Autowired
	OwnershipRepository ownerRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Resource
	MovementWorker worker;

	@Override
	public List<MovementSelectionViewDTO> getMyUnits(String cookie) {
		Player player = playerRepo.findBySid(cookie);
		List<MovementSelectionViewDTO> movements = new ArrayList<>();
		for (Ownership o : player.getOwnedProvinces()) {
			for (Unit unit : o.getUnits()) {
				movements.add(new MovementSelectionViewDTO(o.getProvinceName(),
						unit.getId(), unit.getSize()));
			}
		}

		for (Unit unit : player.getHome().getUnits()) {
			movements.add(new MovementSelectionViewDTO(player.getHome()
					.getProvinceName(), unit.getId(), unit.getSize(),
					ProvinceType.HOME));
		}
		return movements;
	}

	@Override
	public List<MovementSelectionViewDTO> getMyUnits(String lat, String lon,
			String cookie) {
		double latitude = CalculationUtil.normalizeLatitute(lat);
		double longitude = CalculationUtil.normalizeLongitude(lon);
		Player player = playerRepo.findBySid(cookie);
		List<MovementSelectionViewDTO> movements = new ArrayList<>();
		boolean isThisProvince = false;
		for (Ownership o : player.getOwnedProvinces()) {
			isThisProvince = (o.getProvince().getLatitude() == latitude)
					&& (o.getProvince().getLongitude() == longitude);
			if (!isThisProvince) {
				for (Unit unit : o.getUnits()) {
					movements.add(new MovementSelectionViewDTO(o
							.getProvinceName(), unit.getId(), unit.getSize()));
				}
			}
		}
		isThisProvince = (player.getHome().getProvince().getLatitude() == latitude)
				&& (player.getHome().getProvince().getLongitude() == longitude);
		if (!isThisProvince) {
			for (Unit unit : player.getHome().getUnits()) {
				movements.add(new MovementSelectionViewDTO(player.getHome()
						.getProvinceName(), unit.getId(), unit.getSize(),
						ProvinceType.HOME));
			}
		}
		return movements;
	}

	@Override
	public BeginMovementResponse moveUnitsTo(String lat, String lon,
			List<BeginMovementDTO> beginMoveList, String cookie) {
		double latitude = CalculationUtil.normalizeLatitute(lat);
		double longitude = CalculationUtil.normalizeLongitude(lon);

		Player player = playerRepo.findBySid(cookie);
		int playerStrength = player.findPlayerUnitCount();

		Province destination = provRepo.findWithLatLong(latitude, longitude);
		if (destination == null) {
			destination = createNewBotProvince(latitude, longitude,
					playerStrength);
		}
		// If the destination is not the players home AND
		// no ownership exists, then create a bot ownership
		if (!(destination.getId() == player.getHome().getProvince().getId())
				&& (ownerRepo.findByProvinceId(destination.getId()) == null)) {
			createBotOwnership(destination, playerStrength);
		}

		List<Movement> movList = new ArrayList<>();

		for (BeginMovementDTO dto : beginMoveList) {
			movList.add(createMovement(destination, dto, player));
		}
		Date maximum = movList.get(0).getEndDate();
		for (Movement mov : movList) {
			if (mov.getEndDate().getTime() > maximum.getTime()) {
				maximum = mov.getEndDate();
			}
		}
		return new BeginMovementResponse(maximum, ServerResult.OK);
	}

	@Override
	public List<MovementViewDTO> getMyMovements(String cookie) {
		List<Movement> lst = movementRepo.findByPlayerSid(cookie);
		ArrayList<MovementViewDTO> rtrn = new ArrayList<MovementViewDTO>();
		for (Movement m : lst) {
			Ownership a = ownerRepo
					.findByProvinceId(m.getDestination().getId());
			int overall = m.getUnit().getSize();
			boolean attack = false;
			if (playerRepo.findOwner(a.getId()).getId() != m.getPlayer()
					.getId()) {
				attack = true;
			}
			rtrn.add(new MovementViewDTO(m.getId(), a.getProvinceName(),
					overall, attack, m.getEndDate()));
		}
		return rtrn;
	}

	@Override
	public ServerResponse claimUnits(String lat, String lon, String cookie) {
		double latitude = CalculationUtil.normalizeLatitute(lat);
		double longitude = CalculationUtil.normalizeLongitude(lon);

		Ownership ow = ownerRepo.findProvinceOfPlayer(latitude, longitude,
				cookie);
		int newUnits = 0;
		Date curDate = new Date();
		if (ow != null) {
			newUnits = claimOwnershipUnits(ow, curDate);
		} else {
			HomeOwnership home = homeRepo.findHomeProvinceOfPlayer(latitude,
					longitude, cookie);
			if (home == null) {
				return new ServerResponse(ServerResult.FAIL,
						"Not your province");
			}
			newUnits = claimHomeUnits(home, curDate);
		}

		if (newUnits == 0) {
			return new ServerResponse(ServerResult.NO_NEW_UNITS);
		}
		return new ServerResponse(ServerResult.OK, newUnits);
	}

	@Override
	public BeginMovementResponse cancelMovement(int id, String cookie) {
		// TODO Auto-generated method stub
		return null;
	}

	private int claimHomeUnits(HomeOwnership home, Date curDate) {
		int newUnits = addToUnits(home.getUnits(), home.getLastVisit(),
				curDate, home.countUnits());
		home.setLastVisit(curDate);
		homeRepo.save(home);
		return newUnits;
	}

	private int claimOwnershipUnits(Ownership ow, Date curDate) {
		int newUnits = addToUnits(ow.getUnits(), ow.getLastVisit(), curDate,
				ow.countUnits());
		ow.setLastVisit(curDate);
		ownerRepo.save(ow);
		return newUnits;
	}

	private int addToUnits(Set<Unit> units, Date lastVisitDate, Date curDate,
			int unitCount) {
		int newUnits = 0;
		for (Unit u : units) {
			newUnits = GameLogic.generateNewUnits(lastVisitDate, curDate,
					unitCount);
			u.setSize(u.getSize() + newUnits);
			unitRepo.save(u);
		}
		return newUnits;
	}

	private Movement createMovement(Province destination, BeginMovementDTO dto,
			Player player) {
		Unit unit = unitRepo.findOne(dto.getUnitId());
		if (!checkIfPlayerIsOwnerOfUnit(player, unit)) {
			LOG.info("Unauthorized! Player " + player.getUserName()
					+ " tried to move unit " + unit.getId());
			throw new RuntimeException("Unauthorised unit movement!");
		}

		Ownership ow = ownerRepo.findUnitLocation(unit.getId());
		Movement movement;
		if (ow != null) {
			movement = createNormalProvinceMovement(ow, player, destination,
					unit, dto);
		} else {
			HomeOwnership home = homeRepo.findHomeUnitLocation(unit.getId());
			movement = createHomeProvinceMovement(home, player, destination,
					unit, dto);
		}

		movementRepo.save(movement);

		// Scheduling movement
		worker.setEndDate(movement.getEndDate());
		threadPoolTaskScheduler.schedule(worker, movement.getEndDate());
		return movement;
	}

	/**
	 * Creates a new movement when the destination is the players normal
	 * province. Checks if the {@link Player} is really the owner of this
	 * province.
	 * 
	 * @return {@link Movement}
	 */
	private Movement createNormalProvinceMovement(Ownership ow, Player player,
			Province destination, Unit unit, BeginMovementDTO dto) {

		if (unit.getSize() - dto.getUnitSize() < PROVINCE_UNIT_MIN) {
			// Yeah, this is bad error handling, but i don't give a fuck
			LOG.error("Player " + player.getUserName()
					+ " tried to send too many units to province "
					+ ow.toString());
			throw new RuntimeException("There must be at least "
					+ PROVINCE_UNIT_MIN + " units left in province!");
		}

		Unit newUnit = decreaseAndPersistUnit(unit, dto);

		Date curDate = new Date();
		Movement movement = new Movement(newUnit, ow.getProvince(),
				destination, player, curDate, GameLogic.calculateTime(
						ow.getProvince(), destination, curDate));
		return movement;
	}

	/**
	 * Creates a new movement when the destination is the players home province.
	 * Checks if the {@link Player} is really the owner of this province.
	 * 
	 * @return {@link Movement}
	 */
	private Movement createHomeProvinceMovement(HomeOwnership ow,
			Player player, Province destination, Unit unit, BeginMovementDTO dto) {

		if (unit.getSize() - dto.getUnitSize() < 0) {
			// Yeah, this is bad error handling, but i don't give a fuck
			LOG.error("Player " + player.getUserName()
					+ " tried to send too many units to province "
					+ ow.toString());
			throw new RuntimeException(
					"There must be at least 0 units left in home province!");
		}

		Unit newUnit = decreaseAndPersistUnit(unit, dto);

		Date curDate = new Date();
		Movement movement = new Movement(newUnit, ow.getProvince(),
				destination, player, curDate, GameLogic.calculateTime(
						ow.getProvince(), destination, curDate));
		return movement;
	}

	/**
	 * Creates a new {@link Province} at this location and assigns the
	 * {@link Ownership} to the BOT.
	 * 
	 * @param latitude
	 * @param longitude
	 * @return BOT owned {@link Province}
	 */
	private Province createNewBotProvince(double latitude, double longitude,
			int playerStrength) {
		Province province = new Province(latitude, longitude);
		provRepo.save(province);
		LOG.info("Created new province for BOT " + province.toString());
		return createBotOwnership(province, playerStrength);
	}

	/**
	 * Create a new {@link Ownership} for an unowned {@link Province}. Populate
	 * it with units.
	 * 
	 * @param province
	 * @param playerStrength
	 * @return BOT owned {@link Province}
	 */
	private Province createBotOwnership(Province province, int playerStrength) {
		LOG.info("Creating BOT ownership for " + province.toString());
		Unit unit = new Unit(GameLogic.botUnits(province.getLatitude(),
				province.getLongitude(), playerStrength));
		unitRepo.save(unit);
		Ownership ow = new Ownership(province, unit);
		ownerRepo.save(ow);
		Player bot = playerRepo.findOne(0);
		bot.addOwnership(ow);
		playerRepo.save(bot);
		return province;
	}

	/**
	 * Decreases the size of the old {@link Unit} and creates a new {@link Unit}
	 * with {@link BeginMovementDTO#getUnitSize()} as the size
	 * 
	 * @param oldUnit
	 * @param dto
	 * @return created {@link Unit}
	 */
	private Unit decreaseAndPersistUnit(Unit oldUnit, BeginMovementDTO dto) {
		oldUnit.setSize(oldUnit.getSize() - dto.getUnitSize());
		Unit newUnit = new Unit(dto.getUnitSize());
		unitRepo.save(oldUnit);
		unitRepo.save(newUnit);
		return newUnit;
	}

	/**
	 * Checks that the {@link Unit} belongs to this {@link Player}. Checks in
	 * both {@link Ownership} and {@link HomeOwnership}.
	 * 
	 * @param player
	 *            {@link Player}
	 * @param unit
	 *            {@link Unit}
	 * @return {@code true} if it belongs. false if not
	 */
	private boolean checkIfPlayerIsOwnerOfUnit(Player player, Unit unit) {
		Player unitOwner = playerRepo.findOwnerOfUnit(unit.getId());
		if ((unitOwner != null) && (player.getId() == unitOwner.getId())) {
			return true;
		}
		unitOwner = playerRepo.findOwnerOfHomeUnit(unit.getId());
		if ((unitOwner != null) && (player.getId() == unitOwner.getId())) {
			return true;
		}
		return false;
	}

}
