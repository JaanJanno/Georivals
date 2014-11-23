package ee.bmagrupp.georivals.server.core.repository;

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
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;

/**
 * Tests for {@link MovementRepository}
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
public class MovementRepositoryTest {

	@Autowired
	private MovementRepository moveRepo;

	@Autowired
	private ProvinceRepository provRepo;

	@Autowired
	private PlayerRepository playerRepo;

	private Date endDate;

	@Before
	public void setUp() {
		Player player = playerRepo.findOne(1);
		Unit unit = player.getHome().getUnits().iterator().next();
		Province destination = provRepo.findOne(3);
		endDate = new Date();
		Movement mov = new Movement(unit, player.getHome().getProvince(),
				destination, player, new Date(), endDate);
		moveRepo.save(mov);

		Calendar cal = Calendar.getInstance(); // creates calendar
		cal.setTime(endDate); // sets calendar time/date
		cal.add(Calendar.SECOND, 20);

		Player player2 = playerRepo.findOne(2);

		Movement mov2 = new Movement(unit, player2.getHome().getProvince(),
				destination, player2, new Date(), cal.getTime());
		moveRepo.save(mov2);

		// For under attack query

		// Attack movement
		cal.add(Calendar.SECOND, 22);
		Province destination1 = provRepo.findOne(4);
		Movement mov3 = new Movement(unit, player.getHome().getProvince(),
				destination1, player, new Date(), cal.getTime());
		moveRepo.save(mov3);

	}

	@Test
	public void findByDestinationTest() {

		List<Movement> resultList = moveRepo.findByDestination(3);
		assertEquals("Destination id", 3, resultList.get(0).getDestination()
				.getId());
	}

	@Test
	public void checkDestinationTest() {
		boolean result = moveRepo.checkIfDestination(3);
		assertTrue("is a destination", result);

		boolean result2 = moveRepo.checkIfDestination(1);
		assertFalse("is not a destination", result2);
	}

	@Test
	public void findPlayerMovements() {
		List<Movement> resultList = moveRepo
				.findByPlayerSid("BPUYYOU62flwiWJe");
		assertEquals("Destination id", 3, resultList.get(0).getDestination()
				.getId());
		assertEquals("Player id", 1, resultList.get(0).getPlayer().getId());

	}

	@Test
	public void findByEndDateTest() {
		PageRequest pageR = new PageRequest(0, 1);
		List<Movement> mov = moveRepo.findByEndDate(endDate, pageR);
		assertEquals("Player id", 1, mov.get(0).getPlayer().getId());

	}

	@Test
	public void findMostRecent() {
		PageRequest pageR = new PageRequest(0, 1);
		List<Movement> mov = moveRepo.getMostRecent(pageR);
		assertEquals("Player id", 1, mov.get(0).getPlayer().getId());

	}

	@Test
	public void getAllEndDates() {
		List<Date> endDates = moveRepo.getAllEndDates();
		assertTrue("First date is before the second",
				endDates.get(0).before(endDates.get(1)));

	}

	@Test
	public void checkIfUnderAttack() {
		boolean a1 = moveRepo.checkIfUnderAttack(5, 4);
		assertTrue("Province is under attack", a1);

	}

}
