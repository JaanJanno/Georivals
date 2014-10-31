package ee.bmagrupp.aardejaht.server.core.repository;

import static org.junit.Assert.*;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.util.Constants;
import ee.bmagrupp.aardejaht.server.util.GeneratorUtil;

/**
 * Tests for {@link PlayerRepository}
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
public class PlayerRepositoryTest {

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	ProvinceRepository provinceRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	@Test
	public void allPlayers() {
		List<Player> players = (List<Player>) playerRepo.findAll();

		assertEquals("There should be 6 Players", 6, players.size());
	}

	@Test
	public void singlePlayer() {
		Player player = playerRepo.findOne(1);

		assertEquals("Player id", 1, player.getId());
	}

	@Test
	public void ownerTest() {
		Player player = playerRepo.findOwner(1);

		assertEquals("Player owner", 1, player.getId());

		// Test for no such ownership
		Player player2 = playerRepo.findOwner(20);

		assertEquals("Player owner", null, player2);
	}

	/**
	 * Testing the authentication stuff.
	 */
	@Test
	public void authTest() {
		// Player 2 - Doge
		Player p = playerRepo.findBySid("3myBuV7DKARaW14p");

		assertEquals("Player Sid or password", 2, p.getId());

		// No such sid test
		Player p1 = playerRepo.findBySid("HDpVys213");

		assertEquals("There should be no user with this sid", null, p1);
	}

	@Test
	public void emailTest() {
		// Player 1 - Mr. TK
		Player p = playerRepo.findByEmail("mr.tk@pacific.ee");

		assertEquals("Player with this email", 1, p.getId());

		// No such email test
		Player p1 = playerRepo.findByEmail("mr.tk@pacific.ee123");

		assertEquals("There should be no user with this email", null, p1);
	}

	@Test
	public void userNameTest() {
		// Player 2 - Doge
		Player p = playerRepo.findByUserName("Doge");

		assertEquals("Player with this username", 2, p.getId());

		// No such username test
		Player p1 = playerRepo.findByUserName("Random guy");

		assertEquals("There should be no user with this username", null, p1);
	}

	@Test
	public void saveTestSuccess() {
		Province home = new Province(26.123, 58.123);
		Player player = new Player("Smaug", GeneratorUtil.generateString(16), home);

		assertNull(playerRepo.findByUserName("Smaug"));
		provinceRepo.save(home);
		homeRepo.save(player.getHome());
		playerRepo.save(player);

		// Checking
		Player player2 = playerRepo.findByUserName("Smaug");
		assertEquals("Player username", "Smaug", player2.getUserName());
		assertEquals("Player email", null, player2.getEmail());
		assertEquals("Player sid", Constants.PLAYER_SID_LENGTH, player2
				.getSid().length());
		assertEquals("Player owned stuff", 0, player2.getOwnedProvinces()
				.size());
		assertEquals("Player home province units ", null, player2.getHome()
				.getUnits());
		assertEquals("Player home province latitude", 26.123, player2.getHome()
				.getProvince().getLatitude(), 0.001);
		assertEquals("Player home province longitude", 58.123, player2
				.getHome().getProvince().getLongitude(), 0.001);
	}

	@Test(expected = DataIntegrityViolationException.class)
	public void saveUsernameExists() {
		Province home = new Province(26.123, 58.123);
		Player player = new Player("Doge",
				GeneratorUtil.generateString(Constants.PLAYER_SID_LENGTH), home);

		provinceRepo.save(home);
		homeRepo.save(player.getHome());
		playerRepo.save(player);
	}

}
