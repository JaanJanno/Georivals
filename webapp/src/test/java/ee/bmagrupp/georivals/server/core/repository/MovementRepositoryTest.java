package ee.bmagrupp.georivals.server.core.repository;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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

	@Before
	public void setUp() {
		Player player = playerRepo.findOne(1);
		Unit unit = player.getHome().getUnits().iterator().next();
		Province destination = provRepo.findOne(3);
		Movement mov = new Movement(unit, player.getHome().getProvince(),
				destination, player, new Date(), new Date());
		moveRepo.save(mov);
	}

	@Test
	public void findByDestinationTest() {

		List<Movement> resultList = moveRepo.findByDestination(3);
		assertEquals("List size", 1, resultList.size());
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
		assertEquals("List size", 1, resultList.size());
		assertEquals("Destination id", 3, resultList.get(0).getDestination()
				.getId());
		assertEquals("Player id", 1, resultList.get(0).getPlayer().getId());

	}

}
