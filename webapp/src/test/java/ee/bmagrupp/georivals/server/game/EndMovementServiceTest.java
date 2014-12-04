package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Ignore;
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

	@Autowired
	private BattleHistoryRepository batHistRepo;

	@Autowired
	private OwnershipRepository ownerRepo;

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

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());
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

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());
		assertEquals("Units at home", 27, playerRepo.findOne(1).getHome()
				.getUnits().iterator().next().getSize());

	}

	@Test
	public void addToExistingOwnedUnit() {

		// I know it's there
		Date endDate = ownedProvMov.getEndDate();
		endMovServ.handleMovement(endDate);

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());
		assertEquals("Units added to home", 33, unitRepo.findOne(6).getSize());

		// Checking the movement unit was deleted
		List<Unit> units = (List<Unit>) unitRepo.findAll();
		for (Unit unit : units) {
			assertNotEquals("No such unit should still be in the database", 24,
					unit.getSize());
		}
	}

	@Test
	public void battleAttackerWinsTest() {
		// Testing when the attacker will win

		// Setting stuff up
		// Mr.TK will attack Doge
		Player player = playerRepo.findOne(1);
		Unit unit = new Unit(27);
		unitRepo.save(unit);
		Province origin = provRepo.findOne(6);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.SECOND, 30);
		Date endDate = cal.getTime();
		Movement battleMov = new Movement(unit, origin, provRepo.findOne(2),
				player, new Date(), endDate);
		moveRepo.save(battleMov);

		// name change
		Ownership o = ownerRepo.findOne(3);
		o.setProvinceName("Gringotts");
		ownerRepo.save(o);

		endMovServ.handleMovement(endDate);

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());

		List<BattleHistory> list = (List<BattleHistory>) batHistRepo.findAll();

		BattleHistory battle = null;
		for (BattleHistory battleHistory : list) {
			if (battleHistory.getLocation().getId() == 2) {
				battle = battleHistory;
				break;
			}
		}

		assertEquals("Attacker id", 1, battle.getAttacker().getId());
		assertEquals("Defender id", 2, battle.getDefender().getId());
		assertEquals("Battle location", 2, battle.getLocation().getId());
		assertEquals("Attacker strength", 27, battle.getAttackerStrength());
		assertEquals("Defender strength", 2, battle.getDefenderStrength());
		if (battle.isAttackerWon()) {
			Ownership ow = ownerRepo.findByProvinceId(2);
			assertEquals("Attacker controls the province", 1, playerRepo
					.findOwnerOfProvince(2).getId());
			assertEquals("Owner units", 27 - battle.getAttackerLosses(), ow
					.getUnits().iterator().next().getSize());
			assertEquals("Province has old name", "Gringotts",
					ow.getProvinceName());

			// Defender units gone
			assertEquals("No defender units", null, unitRepo.findOne(2));
		}

	}

	@Test
	public void battleDefenderWinsTest() {
		// Doge attacks Mr.TK
		Player player = playerRepo.findOne(2);
		Unit unit = new Unit(1);
		unitRepo.save(unit);
		int unitId = unit.getId();
		Province origin = provRepo.findOne(1);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.SECOND, 35);
		Date endDate = cal.getTime();
		Movement battleMov = new Movement(unit, origin, provRepo.findOne(6),
				player, new Date(), endDate);
		moveRepo.save(battleMov);

		// Making the move happen
		endMovServ.handleMovement(endDate);

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());

		List<BattleHistory> list = (List<BattleHistory>) batHistRepo.findAll();
		BattleHistory battle = null;
		for (BattleHistory battleHistory : list) {
			if ((battleHistory.getLocation().getId() == 6)
					&& (battleHistory.getAttackerStrength() == 1)) {
				battle = battleHistory;
				break;
			}
		}

		assertEquals("Attacker id", 2, battle.getAttacker().getId());
		assertEquals("Defender id", 1, battle.getDefender().getId());
		assertEquals("Battle location", 6, battle.getLocation().getId());
		assertEquals("Attacker strength", 1, battle.getAttackerStrength());
		assertEquals("Defender strength", 9, battle.getDefenderStrength());
		if (!battle.isAttackerWon()) {
			Ownership ow = ownerRepo.findByProvinceId(6);
			assertEquals("Defender controls the province", 2, playerRepo
					.findOwnerOfProvince(2).getId());
			assertEquals("Owner units", 9 - battle.getDefenderLosses(), ow
					.getUnits().iterator().next().getSize());

			// Attacking unit gone
			assertNull("Attacking unit gone", unitRepo.findOne(unitId));

		}

	}

	@Test
	public void attackBotAndWin() {
		// Create bot ownership
		Province prov = new Province(11.1115, 22.222);
		provRepo.save(prov);
		Unit botUnit = new Unit(5);
		unitRepo.save(botUnit);
		Ownership botow = new Ownership(prov, botUnit);
		ownerRepo.save(botow);
		Player bot = playerRepo.findOne(0);
		bot.addOwnership(botow);
		playerRepo.save(bot);

		// create movement
		Player player = playerRepo.findOne(1);
		Unit unit = new Unit(27);
		unitRepo.save(unit);
		Province origin = provRepo.findOne(6);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.SECOND, 22);
		Date endDate = cal.getTime();
		Movement battleMov = new Movement(unit, origin, prov, player,
				new Date(), endDate);
		moveRepo.save(battleMov);

		endMovServ.handleMovement(endDate);

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());

		List<BattleHistory> list = (List<BattleHistory>) batHistRepo.findAll();
		BattleHistory battle = null;
		for (BattleHistory battleHistory : list) {
			if (battleHistory.getLocation().getId() == prov.getId()) {
				battle = battleHistory;
				break;
			}
		}

		assertEquals("Attacker id", 1, battle.getAttacker().getId());
		assertEquals("Defender id", 0, battle.getDefender().getId());
		assertEquals("Battle location", prov.getId(), battle.getLocation()
				.getId());
		assertEquals("Attacker strength", 27, battle.getAttackerStrength());
		assertEquals("Defender strength", 5, battle.getDefenderStrength());
		if (battle.isAttackerWon()) {
			Ownership ow = ownerRepo.findByProvinceId(prov.getId());
			assertEquals("Attacker controls the province", 1, playerRepo
					.findOwnerOfProvince(prov.getId()).getId());
			assertEquals("Owner units", 27 - battle.getAttackerLosses(), ow
					.getUnits().iterator().next().getSize());

			// Defender units gone
			assertNull("No defender units", unitRepo.findOne(botUnit.getId()));
		}

	}

	@Test
	@Ignore
	public void attackBotAndLose() {
		// Create bot ownership
		Province prov = new Province(11.1115, 22.222);
		provRepo.save(prov);
		Unit botUnit = new Unit(15);
		unitRepo.save(botUnit);
		Ownership botow = new Ownership(prov, botUnit);
		ownerRepo.save(botow);
		Player bot = playerRepo.findOne(0);
		bot.addOwnership(botow);
		playerRepo.save(bot);

		// create movement
		Player player = playerRepo.findOne(1);
		Unit unit = new Unit(2);
		unitRepo.save(unit);
		Province origin = provRepo.findOne(6);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(new Date()); // sets calendar time/date
		cal.add(Calendar.SECOND, 21);
		Date endDate = cal.getTime();
		Movement battleMov = new Movement(unit, origin, prov, player,
				new Date(), endDate);
		moveRepo.save(battleMov);

		endMovServ.handleMovement(endDate);

		assertTrue("No such movement",
				moveRepo.findByEndDate(endDate, new PageRequest(0, 1))
						.isEmpty());

		List<BattleHistory> list = (List<BattleHistory>) batHistRepo.findAll();
		BattleHistory battle = null;
		for (BattleHistory battleHistory : list) {
			if (battleHistory.getLocation().getId() == prov.getId()) {
				battle = battleHistory;
				break;
			}
		}

		assertEquals("Attacker id", 1, battle.getAttacker().getId());
		assertEquals("Defender id", 0, battle.getDefender().getId());
		assertEquals("Battle location", prov.getId(), battle.getLocation()
				.getId());
		assertEquals("Attacker strength", 2, battle.getAttackerStrength());
		assertEquals("Defender strength", 15, battle.getDefenderStrength());
		if (!battle.isAttackerWon()) {
			// BOT ownership has been deleted
			assertNull("Attacker controls the province",
					ownerRepo.findByProvinceId(prov.getId()));

			// Defender units gone
			assertNull("No defender units", unitRepo.findOne(botUnit.getId()));
		}

	}

}
