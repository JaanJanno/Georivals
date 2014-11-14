package ee.bmagrupp.georivals.server.service.imlp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
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
	public BeginMovementResponse moveUnitsTo(String lat, String lon,
			List<BeginMovementDTO> beginMoveList, String cookie) {
		double latitude = Double.parseDouble(lat);
		double longitude = Double.parseDouble(lon);

		latitude = CalculationUtil.normalizeLatitute(latitude);
		longitude = CalculationUtil.normalizeLongitude(longitude);

		Player player = playerRepo.findBySid(cookie);
		int playerStrength = player.findPlayerUnitCount();

		Province destination = provRepo.findWithLatLong(latitude, longitude);
		if (destination == null) {
			destination = createNewBotProvince(latitude, longitude,
					playerStrength);
		}

		List<Movement> movList = new ArrayList<>();

		for (BeginMovementDTO dto : beginMoveList) {
			movList.add(createMovement(destination, dto, player));
		}
		// TODO End date calculation
		return new BeginMovementResponse(movList.get(0).getEndDate(),
				ServerResult.OK);
	}

	@Override
	public List<MovementViewDTO> getMyMovements(String cookie) {
		// TODO Auto-generated method stub
		return null;
	}

	private Movement createMovement(Province destination, BeginMovementDTO dto,
			Player player) {
		Unit unit = unitRepo.findOne(dto.getUnitId());
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
		return movement;
	}

	/**
	 * Creates a new movement when the origin is the players normal province.
	 * Checks if the {@link Player} is really the owner of this province.
	 * 
	 * @return {@link Movement}
	 */
	private Movement createNormalProvinceMovement(Ownership ow, Player player,
			Province destination, Unit unit, BeginMovementDTO dto) {
		if (player.getId() != playerRepo.findOne(ow.getId()).getId()) {
			LOG.error("Player " + player.getUserName()
					+ " is not the owner of home province" + ow.toString());
			throw new RuntimeException("Unauthorised unit movement!");
		}

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
	 * Creates a new movement when the origin is the players home province.
	 * Checks if the {@link Player} is really the owner of this province.
	 * 
	 * @return {@link Movement}
	 */
	private Movement createHomeProvinceMovement(HomeOwnership ow,
			Player player, Province destination, Unit unit, BeginMovementDTO dto) {
		if (player.getHome().getId() != ow.getId()) {
			LOG.error("Player " + player.getUserName()
					+ " is not the owner of home province" + ow.toString());
			throw new RuntimeException("Unauthorised unit movement!");
		}

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
		Unit unit = new Unit(GameLogic.botUnits(latitude, longitude,
				playerStrength));
		unitRepo.save(unit);
		Ownership ow = new Ownership(province, unit);
		ownerRepo.save(ow);
		Player bot = playerRepo.findOne(0);
		bot.addOwnership(ow);
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

}
