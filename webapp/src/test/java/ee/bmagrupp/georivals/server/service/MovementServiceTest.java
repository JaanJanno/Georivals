package ee.bmagrupp.georivals.server.service;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import ee.bmagrupp.georivals.server.core.domain.HomeOwnership;
import ee.bmagrupp.georivals.server.core.domain.Movement;
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.MovementRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.georivals.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.MovementViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.rest.domain.ServerResult;

/**
 * Tests for {@link MovementService}
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
public class MovementServiceTest {

	@Autowired
	MovementService movServ;

	@Autowired
	MovementRepository movRepo;

	@Autowired
	UnitRepository unitRepo;

	@Autowired
	PlayerRepository playerRepo;

	@Autowired
	ProvinceRepository provRepo;

	@Autowired
	HomeOwnershipRepository homeRepo;

	private double latitude;
	private double longitude;
	private String sid;

	@Before
	public void setUp() {
		sid = "BPUYYOU62flwiWJe";
		latitude = 24.4525;
		longitude = 54.321;
	}

	@Test
	public void getMyMovementsTest() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(7, 6));
		double lat = 13.1235;
		double lon = 10.567;

		BeginMovementResponse response = movServ.moveUnitsTo(
				Double.toString(lat), Double.toString(lon), list,
				"BPUYYOU62flwiWJe");
		assertEquals("Result is ok", ServerResult.OK, response.getResult());

		List<MovementViewDTO> lst = movServ.getMyMovements("BPUYYOU62flwiWJe");
		assertEquals("list should contain 1 element", 1, lst.size());
		assertEquals("it is an attack", true, lst.get(0).isAttack());
	}

	@Test
	public void claimUnitsTest() {
		Set<Ownership> lst = playerRepo.findBySid(sid).getOwnedProvinces();
		Province a = null;
		int value = 0;
		for (Ownership o : lst) {
			a = o.getProvince();
			value = o.countUnits();
			break;
		}
		ServerResponse resp = movServ.claimUnits(
				String.valueOf(a.getLatitude()),
				String.valueOf(a.getLongitude()), sid);
		assertEquals("Expected: ", ServerResult.OK, resp.getResult());

		lst = playerRepo.findBySid(sid).getOwnedProvinces();
		for (Ownership o : lst) {
			assertNotEquals("Expected", value, o.countUnits());
			break;
		}
	}

	@Test
	public void claimHomeUnitsTest() {
		HomeOwnership home = playerRepo.findBySid(sid).getHome();
		int value = home.countUnits();

		ServerResponse resp = movServ.claimUnits(
				String.valueOf(home.getProvince().getLatitude()),
				String.valueOf(home.getProvince().getLongitude()), sid);
		assertEquals("Expected: ", ServerResult.OK, resp.getResult());

		home = playerRepo.findBySid(sid).getHome();
		assertEquals("Expected", value + resp.getId(), home.countUnits());
	}

	@Test
	public void claimUnitsNoNewUnits() {
		HomeOwnership home = playerRepo.findBySid(sid).getHome();
		home.setLastVisit(new Date());
		homeRepo.save(home);
		int value = home.countUnits();

		ServerResponse resp = movServ.claimUnits(
				String.valueOf(home.getProvince().getLatitude()),
				String.valueOf(home.getProvince().getLongitude()), sid);
		assertEquals("Expected: ", ServerResult.NO_NEW_UNITS, resp.getResult());

		home = playerRepo.findBySid(sid).getHome();
		assertEquals("Expected", value + resp.getId(), home.countUnits());
	}

	@Test
	public void getMyUnitsTest() {
		List<MovementSelectionViewDTO> dtos = movServ
				.getMyUnits("BPUYYOU62flwiWJe"); // Mr.TK

		assertEquals("The number of units this player has", 2, dtos.size());

		assertEquals("Unit type", ProvinceType.PLAYER, dtos.get(0).getType());
		assertEquals("Unit size", 9, dtos.get(0).getUnitSize());
		assertEquals("Unit id", 6, dtos.get(0).getUnitId());
		assertEquals("Unit province name", "Kvukx9SCOB", dtos.get(0)
				.getProvinceName());

		// Home province
		assertEquals("Unit size", 10, dtos.get(1).getUnitSize());
		assertEquals("Unit province name", "lzpD6mFm44", dtos.get(1)
				.getProvinceName());
		assertEquals("Unit unit id", 7, dtos.get(1).getUnitId());
		assertEquals("Unit type", ProvinceType.HOME, dtos.get(1).getType());
	}

	@Test
	public void getMyUnitsExcludeNormalProvince() {
		List<MovementSelectionViewDTO> dtos = movServ.getMyUnits("-40.4225",
				"144.963", "BPUYYOU62flwiWJe"); // Mr.TK

		assertEquals("The number of units this player has", 1, dtos.size());

		// Home province
		assertEquals("Unit size", 10, dtos.get(0).getUnitSize());
		assertEquals("Unit province name", "lzpD6mFm44", dtos.get(0)
				.getProvinceName());
		assertEquals("Unit unit id", 7, dtos.get(0).getUnitId());
		assertEquals("Unit type", ProvinceType.HOME, dtos.get(0).getType());
	}

	@Test
	public void getMyUnitsExcludeHome() {
		List<MovementSelectionViewDTO> dtos = movServ.getMyUnits("-40.4195",
				"144.961", "BPUYYOU62flwiWJe"); // Mr.TK

		assertEquals("The number of units this player has", 1, dtos.size());

		assertEquals("Unit type", ProvinceType.PLAYER, dtos.get(0).getType());
		assertEquals("Unit size", 9, dtos.get(0).getUnitSize());
		assertEquals("Unit id", 6, dtos.get(0).getUnitId());
		assertEquals("Unit province name", "Kvukx9SCOB", dtos.get(0)
				.getProvinceName());
	}

	@Test
	public void beginMovementTest() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(6, 6));

		// Precheck
		assertEquals("Unit size", 9, unitRepo.findOne(6).getSize());

		BeginMovementResponse response = movServ.moveUnitsTo(
				Double.toString(latitude), Double.toString(longitude), list,
				"BPUYYOU62flwiWJe");

		assertEquals("Result is ok", ServerResult.OK, response.getResult());

		// Checking for decreased unit size
		assertEquals("Unit size", 3, unitRepo.findOne(6).getSize());

		List<Movement> movList = (List<Movement>) movRepo.findAll();
		// The movement made has to be the last one
		Movement mov = movList.get(movList.size() - 1);
		assertEquals("Origin id", 6, mov.getOrigin().getId());
		assertEquals("Destination lat", latitude, mov.getDestination()
				.getLatitude(), 0.0001);
		assertEquals("Destination longitude", longitude, mov.getDestination()
				.getLongitude(), 0.0001);
		assertEquals("Player id", 1, mov.getPlayer().getId());
		assertEquals("New Unit size", 6, mov.getUnit().getSize());
		// Dates won't be tested because they kinda change all the time

	}

	@Test
	public void beginHomeMovementTest() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(7, 6));
		double lat = -40.4195;
		double lon = 144.963;

		// Precheck
		assertEquals("Unit size", 10, unitRepo.findOne(7).getSize());

		BeginMovementResponse response = movServ.moveUnitsTo(
				Double.toString(lat), Double.toString(lon), list,
				"BPUYYOU62flwiWJe");

		assertEquals("Result is ok", ServerResult.OK, response.getResult());

		// Checking for decreased unit size
		assertEquals("Unit size", 4, unitRepo.findOne(7).getSize());

		List<Movement> movList = (List<Movement>) movRepo.findAll();
		// The movement made has to be the last one
		Movement mov = movList.get(movList.size() - 1);
		assertEquals("Origin id", 1, mov.getOrigin().getId());
		assertEquals("Destination id", 2, mov.getDestination().getId(), 0.0001);
		assertEquals("Destination lat", lat,
				mov.getDestination().getLatitude(), 0.0001);
		assertEquals("Destination longitude", lon, mov.getDestination()
				.getLongitude(), 0.0001);
		assertEquals("Player id", 1, mov.getPlayer().getId());
		assertEquals("New Unit size", 6, mov.getUnit().getSize());
		// Dates won't be tested because they kinda change all the time

	}

	@Test
	public void moveToBotProvince() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(7, 6));
		double lat = 13.1235;
		double lon = 10.567;

		BeginMovementResponse response = movServ.moveUnitsTo(
				Double.toString(lat), Double.toString(lon), list,
				"BPUYYOU62flwiWJe");

		assertEquals("Result is ok", ServerResult.OK, response.getResult());

		// Checking for decreased unit size
		assertEquals("Unit size", 4, unitRepo.findOne(7).getSize());

		List<Movement> movList = (List<Movement>) movRepo.findAll();
		// The movement made has to be the last one
		Movement mov = movList.get(movList.size() - 1);
		assertEquals("Origin id", 1, mov.getOrigin().getId());
		assertEquals("Destination lat", lat,
				mov.getDestination().getLatitude(), 0.0001);
		assertEquals("Destination longitude", lon, mov.getDestination()
				.getLongitude(), 0.0001);
		assertEquals("Player id", 1, mov.getPlayer().getId());
		assertEquals("New Unit size", 6, mov.getUnit().getSize());

		// Checking that the BOT has ownership
		Player bot = playerRepo.findOne(0);
		assertEquals("BOT has one ownerhip", 1, bot.getOwnedProvinces().size());
		assertEquals("BOT ownership has movement destination", mov
				.getDestination().getId(), bot.getOwnedProvinces().iterator()
				.next().getProvince().getId());
		assertNotNull("BOT has units in that province", bot.getOwnedProvinces()
				.iterator().next().getUnits().iterator().next());
	}

	@Test(expected = RuntimeException.class)
	public void beginMovementTestTooManyUnits() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(6, 10));

		movServ.moveUnitsTo(Double.toString(latitude),
				Double.toString(longitude), list, "BPUYYOU62flwiWJe");

	}

	@Test(expected = RuntimeException.class)
	public void beginMovementWrongUser() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(6, 6));

		// Precheck
		assertEquals("Unit size", 9, unitRepo.findOne(6).getSize());

		movServ.moveUnitsTo(Double.toString(latitude),
				Double.toString(longitude), list, "3myBuV7DKARaW14p");

	}

	@Test(expected = RuntimeException.class)
	public void beginHomeMovementWrongUser() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(7, 6));

		// Precheck
		assertEquals("Unit size", 10, unitRepo.findOne(7).getSize());

		movServ.moveUnitsTo(Double.toString(latitude),
				Double.toString(longitude), list, "3myBuV7DKARaW14p");

	}

	@Test(expected = NullPointerException.class)
	public void beginMovementNoUser() {
		List<BeginMovementDTO> list = new ArrayList<>();
		list.add(new BeginMovementDTO(6, 6));

		// Precheck
		assertEquals("Unit size", 9, unitRepo.findOne(6).getSize());

		movServ.moveUnitsTo(Double.toString(latitude),
				Double.toString(longitude), list, "cookie");

		assertEquals("Unit size", 9, unitRepo.findOne(6).getSize());

	}

}
