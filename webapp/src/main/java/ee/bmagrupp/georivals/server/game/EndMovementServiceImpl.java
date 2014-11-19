package ee.bmagrupp.georivals.server.game;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.BattleHistory;
import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.BattleHistoryRepository;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.util.Constants;

@Transactional
@Service
public class EndMovementServiceImpl implements EndMovementService {

	private static Logger LOG = LoggerFactory
			.getLogger(EndMovementServiceImpl.class);
	@Autowired
	MovementRepository movRepo;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	OwnershipRepository ownerRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Autowired
	UnitRepository unitRepo;

	@Autowired
	BattleLogic battleLog;

	@Autowired
	BattleHistoryRepository batHistRepo;

	@Override
	public void handleMovement(Date endDate) {
		LOG.info("Ending movement " + endDate);
		PageRequest page = new PageRequest(0, 1);
		List<Movement> movList = movRepo.findByEndDate(endDate, page);

		Movement mov = null;
		if (movList.size() == 1) {
			mov = movList.get(0);
		} else {
			LOG.error("No movement was found!");
			// can't throw anything because it's in a separate thread
			return;
		}

		// Checking who owns destination
		// Hibernate.initialize(mov.getPlayer());
		if (mov.getPlayer().getHome().getProvince().getId() == mov
				.getDestination().getId()) {
			// handle home unit addition
			handleHomeAddition(mov, mov.getPlayer().getHome());
			return;
		}
		Ownership ow = ownerRepo.findByProvinceId(mov.getDestination().getId());

		if (mov.getPlayer().getId() == playerRepo.findOwner(ow.getId()).getId()) {
			// handle owned province addition
			handleOwnershipAddition(mov, ow);
		} else {
			// handle battle
			handleBattle(mov, ow);
		}

		LOG.info("Movement ended");

	}

	private void handleHomeAddition(Movement mov, HomeOwnership home) {
		LOG.info("Adding units to home with id " + home.getId() + ". Adding "
				+ mov.getUnit().getSize() + " units");
		Unit homeUnit;

		if (home.getUnits().size() == 0) {
			homeUnit = mov.getUnit();
			home.addUnit(homeUnit);
			homeRepo.save(home);
			movRepo.delete(mov);
			return;
		}
		if (home.getUnits().size() > 1) {
			// TODO something must be done here
			LOG.error("Multiple units at home with id " + home.getId());
		}
		homeUnit = home.getUnits().iterator().next();
		addToExisting(homeUnit, mov);

	}

	private void handleOwnershipAddition(Movement mov, Ownership ow) {
		LOG.info("Adding units to ownership with id " + ow.getId()
				+ ". Adding " + mov.getUnit().getSize() + " units");
		Unit unit;

		if (ow.getUnits().size() == 0) {
			// TODO this is pretty bad
			LOG.error("There shouldn't be 0 units at location " + ow.getId());
			unit = mov.getUnit();
			ow.addUnit(unit);
			ownerRepo.save(ow);
			movRepo.delete(mov);
			return;
		}
		if (ow.getUnits().size() > 1) {
			// TODO something must be done here
			LOG.error("Multiple units at ownership with id " + ow.getId());
		}
		unit = ow.getUnits().iterator().next();
		addToExisting(unit, mov);

	}

	private void handleBattle(Movement mov, Ownership ow) {
		Player defender = playerRepo.findOwner(ow.getId());
		BattleHistory history = battleLog.battle(ow.getProvince(),
				mov.getPlayer(), defender, mov.getUnit().getSize(),
				ow.countUnits());

		if (history.isAttackerWon()) {
			attackerWon(history, mov, ow);
		} else {
			defenderWon(history, mov, ow);
		}

		LOG.info(history.toString());
		// assuming attacker won
		// the attacker must now controll this province
		// the defenders ownership must be destroyed
		// the defenders unit must be destroyed
		// the movement unit must be added to ownership
		// movement must be deleted
		batHistRepo.save(history);
		movRepo.delete(mov);
	}

	/**
	 * Called when the {@link Movement} initiator wins the battle. Deletes the
	 * defenders {@link Unit}'s and {@link Ownership}. Creates a new
	 * {@link Ownership} for the attacker. Movement {@link Unit}'s are resized
	 * and added to the new {@link Ownership}.
	 * 
	 * @param history
	 *            {@link BattleHistory}
	 * @param mov
	 *            {@link Movement}
	 * @param ow
	 *            {@link Ownership}
	 */
	private void attackerWon(BattleHistory history, Movement mov, Ownership ow) {
		// delete defender units
		deleteDefender(history, ow);

		// resize attacker unit
		mov.getUnit().increaseSize(-history.getAttackerLosses());
		unitRepo.save(mov.getUnit());
		// create new ownership
		Ownership newOw = new Ownership(mov.getDestination(), mov.getUnit());
		ownerRepo.save(newOw);
		history.getAttacker().addOwnership(newOw);
		playerRepo.save(history.getAttacker());

	}

	/**
	 * Called when the defender wins. Decreases the size of the defenders
	 * {@link Unit}. Deletes the attackers {@link Unit}. If the defender was the
	 * BOT, deletes the {@link Ownership} and {@link Province}
	 * 
	 * @param history
	 *            {@link BattleHistory}
	 * @param mov
	 *            {@link Movement}
	 * @param ow
	 *            {@link Ownership}
	 */
	private void defenderWon(BattleHistory history, Movement mov, Ownership ow) {

		// delete movement unit
		unitRepo.delete(mov.getUnit());

		if (history.getDefender().getUserName().equals(Constants.BOT_NAME)) {
			LOG.info("Deleting BOT ownership and unit");
			deleteDefender(history, ow);
		} else {
			// decrease defenders unit
			ow.getUnits().iterator().next()
					.increaseSize(-history.getDefenderLosses());
			unitRepo.save(ow.getUnits());
		}
	}

	/**
	 * Deletes the defenders {@link Ownership} and {@link Unit}.
	 * 
	 * @param history
	 *            {@link BattleHistory}
	 * @param ow
	 *            {@link Ownership}
	 */
	private void deleteDefender(BattleHistory history, Ownership ow) {
		LOG.info("Deleting the defenders ownership and unit ");
		unitRepo.delete(ow.getUnits());
		ow.getUnits().clear();
		history.getDefender().getOwnedProvinces().remove(ow);
		ownerRepo.delete(ow);
		playerRepo.save(history.getDefender());
	}

	/**
	 * Increases the size of the parameter {@link Unit}. Saves the {@link Unit}.
	 * Deletes {@link Movement} and it's {@link Unit}.
	 * 
	 * @param unit
	 *            {@link Unit} to increase
	 * @param mov
	 *            {@link Movement} where a {@link Unit} is
	 */
	private void addToExisting(Unit unit, Movement mov) {
		unit.increaseSize(mov.getUnit().getSize());
		normalizeUnits(unit);
		unitRepo.save(unit);
		movRepo.delete(mov);
		unitRepo.delete(mov.getUnit());
	}

	private void normalizeUnits(Unit unit) {
		if (unit.getSize() > Constants.PROVINCE_UNIT_MAX) {
			LOG.info("Size over limit for unit " + unit.getId());
			unit.setSize(Constants.PROVINCE_UNIT_MAX);
		}
	}

	@Override
	public Date getNextMovement() {
		PageRequest page = new PageRequest(0, 1);
		List<Movement> mov = movRepo.getMostRecent(page);
		if (mov.size() == 0)
			return null;
		return mov.get(0).getEndDate();
	}

}
