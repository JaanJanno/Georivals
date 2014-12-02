package ee.bmagrupp.georivals.server.game;

import static org.junit.Assert.*;

import java.util.Date;

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
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;

/**
 * Tests for {@link PlayerService}
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
public class PlayerServiceTest {

	@Autowired
	PlayerService playerServ;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	UnitRepository unitRepo;

	@Autowired
	MovementRepository moveRepo;

	@Autowired
	ProvinceRepository provRepo;

	@Before
	public void setUp() {
		Player player = playerRepo.findOne(1);
		Unit unit = new Unit(27);
		unitRepo.save(unit);
		Province origin = provRepo.findOne(6);
		Movement homeMov = new Movement(unit, origin, player.getHome()
				.getProvince(), player, new Date(), new Date());
		moveRepo.save(homeMov);
	}

	@Test
	public void playerStrengthNoMovements() {
		int strength = playerServ
				.calculatePlayerStrength(playerRepo.findOne(2));
		assertEquals("Player units", 12, strength);
	}

	@Test
	public void playerStrengthWithMovements() {
		int strength = playerServ
				.calculatePlayerStrength(playerRepo.findOne(1));
		assertEquals("Player units", 46, strength);
	}

}
