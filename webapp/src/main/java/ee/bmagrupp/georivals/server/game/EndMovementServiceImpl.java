package ee.bmagrupp.georivals.server.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.util.Constants;

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

	@Override
	public void handleMovement(int movementId) {
		LOG.info("Ending movement " + movementId);
		Movement mov = movRepo.findOne(movementId);

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
		LOG.info("Battle at " + ow.getId());
		// TODO implement this

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

}