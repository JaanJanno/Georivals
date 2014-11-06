package ee.bmagrupp.aardejaht.server.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

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

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.core.domain.Province;
import ee.bmagrupp.aardejaht.server.core.domain.Unit;
import ee.bmagrupp.aardejaht.server.core.repository.PlayerRepository;
import ee.bmagrupp.aardejaht.server.core.repository.ProvinceRepository;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationResponse;
import ee.bmagrupp.aardejaht.server.util.Constants;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

/**
 * Integration tests for {@link RegistrationService}. This service is fully
 * covered by tests.
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
public class RegistrationServiceTest {

	@Autowired
	RegistrationService regServ;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	ProvinceRepository provRepo;

	@Test
	public void phase1success() {
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Smaug");

		RegistrationResponse response = regServ.registrationPhase1(dto);
		assertEquals("Response", ServerResult.OK, response.getResult());
		assertEquals("Value", null, response.getValue());
	}

	@Test
	public void phase1fail() {
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Doge");

		RegistrationResponse response = regServ.registrationPhase1(dto);
		assertEquals("Response", ServerResult.USERNAME_IN_USE,
				response.getResult());
		assertEquals("Value", null, response.getValue());
	}

	@Test
	public void phase2SuccessNewProvince() {
		// Setup
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Smaug");
		dto.setHomeLat(58.123);
		dto.setHomeLong(26.123);

		// Check if user is not in database
		Player p = playerRepo.findByUserName("Smaug");
		assertNull(p);

		RegistrationResponse response = regServ.registrationPhase2(dto);

		// Checking the database for this user
		Player player = playerRepo.findByUserName("Smaug");
		assertNotNull(player);

		assertEquals("Value", player.getSid(), response.getValue());
		assertEquals("Response", ServerResult.OK, response.getResult());
		assertEquals("Player id", player.getId(), response.getId());
	}

	@Test
	public void phase2SuccessExistingProvince() {
		// Setup
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Smaug");
		dto.setHomeLat(58.123);
		dto.setHomeLong(26.123);

		// Create this province already
		Province prov = new Province(26.123, 58.123);
		provRepo.save(prov);

		// Check if user is not in database
		Player p = playerRepo.findByUserName("Smaug");
		assertNull(p);

		RegistrationResponse response = regServ.registrationPhase2(dto);

		// Checking the database for this user
		Player player = playerRepo.findByUserName("Smaug");
		assertNotNull(player);

		// Response check
		assertEquals("Value", player.getSid(), response.getValue());
		assertEquals("Response", ServerResult.OK, response.getResult());
		assertEquals("Player id", player.getId(), response.getId());
	}

	@Test
	public void phase2SuccessEmail() {
		// Setup
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Smaug");
		dto.setHomeLat(58.123);
		dto.setHomeLong(26.123);
		dto.setEmail("smaug@lonelymountain.com");

		// Check if user is not in database
		Player p = playerRepo.findByUserName("Smaug");
		assertNull(p);

		RegistrationResponse response = regServ.registrationPhase2(dto);

		// Checking the database for this user
		Player player = playerRepo.findByUserName("Smaug");
		assertNotNull(player);

		assertEquals("Value", player.getSid(), response.getValue());
		assertEquals("Response", ServerResult.OK, response.getResult());
		assertEquals("Player id", player.getId(), response.getId());
	}

	@Test
	public void phase2failUsernameInUse() {
		// Setup
		RegistrationDTO dto = new RegistrationDTO();
		dto.setUserName("Doge"); // In use
		dto.setHomeLat(58.123);
		dto.setHomeLong(26.123);

		RegistrationResponse response = regServ.registrationPhase2(dto);

		assertEquals("Value", null, response.getValue());
		assertEquals("Response", ServerResult.USERNAME_IN_USE,
				response.getResult());
		assertEquals("Player id", 0, response.getId());
	}

	@Test
	public void createPlayerTest() {
		// Check if user is not in database
		Player p = playerRepo.findByUserName("Smaug");
		assertNull(p);

		Player player = regServ.createPlayer("Smaug",
				"smaug@lonelymountain.com", 58.12332634, 26.12309275);

		// Checking the database for this user
		Player playerCheck = playerRepo.findByUserName("Smaug");
		assertNotNull(playerCheck);

		assertEquals("Player username", "Smaug", player.getUserName());
		assertEquals("Player email", "smaug@lonelymountain.com",
				player.getEmail());
		assertEquals("Player sid", Constants.PLAYER_SID_LENGTH, player.getSid()
				.length());
		assertEquals("Player owned stuff", 0, player.getOwnedProvinces().size());

		assertEquals("Player home province latitude", 58.1235, player.getHome()
				.getProvince().getLatitude(), 0.0001);
		assertEquals("Player home province longitude", 26.123, player.getHome()
				.getProvince().getLongitude(), 0.0005);

		// Unit check
		assertEquals("Player home province units ", 1, player.getHome()
				.getUnits().size());
		Unit unit = player.getHome().getUnits().iterator().next();
		assertEquals("Unit size", Constants.PLAYER_START_UNITS, unit.getSize());

	}
	
	/**
	 * @author Sander Tiganik
	 */
	@Test
	public void createPlayerTest2(){
		String username = "LollipopGuildMaster";
		String email = "Willy@Wonka.gm";
		double lat = 35.3605653;
		double long1 = 138.7277694;
		
		Player player = regServ.createPlayer(username, email, lat, long1);
		lat = player.getHome().getProvince().getLatitude();
		long1 = player.getHome().getProvince().getLongitude();
		
		assertEquals("Latitude", 35.3605, lat, 0.0001);
		assertEquals("Longitude", 138.727, long1, 0.0005);
	}

}
