package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;

/**
 * Tests for {@link EndMovementService}
 * 
 * @author TKasekamp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class EndMovementServiceTest {

	@Autowired
	private MovementRepository moveRepo;

	@Autowired
	private ProvinceRepository provRepo;

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private UnitRepository unitRepo;

	@Autowired
	private EndMovementService endMovServ;

	@Autowired
	private HomeOwnershipRepository homeRepo;

	Movement homeMov;
	Movement ownedProvMov;

	@Before
	public void setUp() {
		Player player = playerRepo.findOne(1);
		Unit unit = new Unit(27);
		unitRepo.save(unit);
		Province origin = provRepo.findOne(6);
		homeMov = new Movement(unit, origin, player.getHome().getProvince(),
				player, new Date(), new Date());
		moveRepo.save(homeMov);

		
		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.SECOND, 20);
		Unit newUnit = new Unit(24);
		unitRepo.save(newUnit);
		Province destination = provRepo.findOne(6);
		ownedProvMov = new Movement(newUnit, player.getHome().getProvince(),
				destination, player, new Date(), cal.getTime());
		moveRepo.save(ownedProvMov);
	}

	@Test
	public void addToExistingHomeUnit() {
		// Precheck
		assertEquals("Number of units at home", 10, unitRepo.findOne(7)
				.getSize());

		// I know it's there
		Date endDate = homeMov.getEndDate();
		endMovServ.handleMovement(endDate);

		assertTrue("No such movement", moveRepo.findByEndDate(endDate, new PageRequest(0, 1)).isEmpty());
		assertEquals("Units added to home", 37, unitRepo.findOne(7).getSize());

		// Checking the movement unit was deleted
		List<Unit> units = (List<Unit>) unitRepo.findAll();
		for (Unit unit : units) {
			assertNotEquals("No such unit should still be in the database", 27,
					unit.getSize());
		}

	}

	@Test
	public void addToNonExistingHomeUnit() {
		// Delete home unit
		playerRepo.findOne(1).getHome().getUnits().clear();
		HomeOwnership home = homeRepo.findOne(1);
		home.getUnits().clear();
		homeRepo.save(home);
		unitRepo.delete(7);
		// Precheck
		assertEquals("No unit at home", 0, homeRepo.findOne(1).getUnits()
				.size());
		assertEquals("No unit at home", null, unitRepo.findOne(7));

		// I know it's there
		Date endDate = homeMov.getEndDate();
		endMovServ.handleMovement(endDate);

		assertTrue("No such movement", moveRepo.findByEndDate(endDate, new PageRequest(0, 1)).isEmpty());
		assertEquals("Units at home", 27, playerRepo.findOne(1).getHome()
				.getUnits().iterator().next().getSize());

	}

	@Test
	public void addToExistingOwnedUnit() {

		// I know it's there
		Date endDate = ownedProvMov.getEndDate();
		endMovServ.handleMovement(endDate);

		assertTrue("No such movement", moveRepo.findByEndDate(endDate, new PageRequest(0, 1)).isEmpty());
		assertEquals("Units added to home", 33, unitRepo.findOne(6).getSize());

		// Checking the movement unit was deleted
		List<Unit> units = (List<Unit>) unitRepo.findAll();
		for (Unit unit : units) {
			assertNotEquals("No such unit should still be in the database", 24,
					unit.getSize());
		}
	}

}
