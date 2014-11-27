package ee.bmagrupp.georivals.server.service;

import static org.junit.Assert.*;

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
import ee.bmagrupp.georivals.server.core.domain.Ownership;
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.domain.Province;
import ee.bmagrupp.georivals.server.core.domain.Unit;
import ee.bmagrupp.georivals.server.core.repository.HomeOwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.OwnershipRepository;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.core.repository.ProvinceRepository;
import ee.bmagrupp.georivals.server.core.repository.UnitRepository;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.ProvinceService;
import ee.bmagrupp.georivals.server.util.Constants;
import ee.bmagrupp.georivals.server.util.GeneratorUtil;
import ee.bmagrupp.georivals.server.util.ServerResult;

/**
 * The {@link ProvinceServiceGeneratorTest} tests are mostly focused on the
 * generation {@link ProvinceDTO}'s, but these tests are focused on the other
 * methods in {@link ProvinceService}.
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
public class ProvinceServiceTest {

	private String sid; // Mr.TK
	private String cookie; // Default cookie value
	private double lat1;
	private double long1;

	private double lat2;
	private double long2;

	@Autowired
	private ProvinceService provServ;

	@Autowired
	private ProvinceRepository provRepo;

	@Autowired
	private PlayerRepository playerRepo;

	@Autowired
	private HomeOwnershipRepository homeRepo;

	@Autowired
	private OwnershipRepository ownerRepo;

	@Autowired
	private UnitRepository unitRepo;

	@Before
	public void setUp() {
		sid = "BPUYYOU62flwiWJe";
		cookie = "cookie";

		lat1 = 37.8265;
		long1 = -122.423;

		lat2 = -40.4195;
		long2 = 144.961;
	}

	// Tests for ChangeHomeProvince

	@Test
	public void testChangeHome() {
		String name = playerRepo.findBySid(sid).getHome().getProvinceName();
		ServerResponse resp = provServ.changeHomeProvince("127.54690235",
				"45.598325", sid);
		assertEquals(ServerResult.OK, resp.getResult());

		HomeOwnership temp = playerRepo.findBySid(sid).getHome();
		assertEquals(127.5465, temp.getProvince().getLatitude(), 0.0001);
		assertEquals(45.599, temp.getProvince().getLongitude(), 0.001);
		assertEquals("names have to persist", name, temp.getProvinceName());
	}

	@Test
	public void changeHomeTest2() {
		Ownership o = playerRepo.findBySid(sid).getOwnedProvinces().iterator()
				.next();
		String lat = String.valueOf(o.getProvince().getLatitude());
		String long1 = String.valueOf(o.getProvince().getLongitude());

		ServerResponse resp = provServ.changeHomeProvince(lat, long1, sid);
		assertEquals("OK", ServerResult.OK, resp.getResult());

		HomeOwnership home = playerRepo.findBySid(sid).getHome();
		double latValue = Double.valueOf(lat);
		double longValue = Double.valueOf(long1);
		assertEquals("latitude", latValue, home.getProvince().getLatitude(),
				0.0001);
		assertEquals("longitude", longValue, home.getProvince().getLongitude(),
				0.0001);
	}

	@Test
	public void changeHomeTest3() {
		String lat = "45.6545";
		String long1 = "98.123";
		double latValue = Double.valueOf(lat);
		double longValue = Double.valueOf(long1);
		provRepo.save(new Province(latValue, longValue));

		ServerResponse resp = provServ.changeHomeProvince(lat, long1, sid);
		assertEquals("OK", ServerResult.OK, resp.getResult());

		HomeOwnership home = playerRepo.findBySid(sid).getHome();
		assertEquals("latitude", latValue, home.getProvince().getLatitude(),
				0.0001);
		assertEquals("longitude", longValue, home.getProvince().getLongitude(),
				0.0001);
	}

	@Test
	public void testChangeName() {
		// Owned Province
		ServerResponse resp = provServ.renameProvince("-40.4225", "144.963",
				"testing", sid);
		assertEquals(ServerResult.OK, resp.getResult());

		Ownership a = ownerRepo.findByProvinceId(provRepo.findWithLatLong(
				-40.4225, 144.963).getId());
		assertEquals("testing", a.getProvinceName());

		// Home province
		ServerResponse resp2 = provServ.renameProvince("-40.4195", "144.961",
				"testing2", sid);
		assertEquals(ServerResult.OK, resp2.getResult());

		HomeOwnership a2 = playerRepo.findBySid(sid).getHome();
		assertEquals("testing2", a2.getProvinceName());

		// Test error
		ServerResponse resp3 = provServ.renameProvince("75.686", "32.533",
				"testing2", sid);
		assertEquals(ServerResult.FAIL, resp3.getResult());

		// Test error2
		ServerResponse resp4 = provServ.renameProvince("-40.4195", "144.965",
				"testing", sid);
		assertEquals(ServerResult.FAIL, resp4.getResult());
	}

	// Tests for getProvince

	@Test
	public void botOwnedProvinceWithSID() {

		ProvinceDTO prov1 = provServ.getProvince(Double.toString(lat1),
				Double.toString(long1), sid);
		ProvinceDTO prov2 = provServ.getProvince(Double.toString(lat1),
				Double.toString(long1), sid);

		assertEquals("Province latitude", lat1, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", long1, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.BOT, prov1.getType());

		assertEquals("Same name", prov1.getProvinceName(),
				prov2.getProvinceName());
		assertEquals("Same unit size", prov1.getUnitSize(), prov2.getUnitSize());
	}

	@Test
	public void botOwnedProvinceWithoutSID() {

		ProvinceDTO prov1 = provServ.getProvince(Double.toString(lat1),
				Double.toString(long1), cookie);
		ProvinceDTO prov2 = provServ.getProvince(Double.toString(lat1),
				Double.toString(long1), cookie);

		assertEquals("Province latitude", lat1, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", long1, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.BOT, prov1.getType());

		assertEquals("Same name", prov1.getProvinceName(),
				prov2.getProvinceName());
		assertEquals("Same unit size", prov1.getUnitSize(), prov2.getUnitSize());
	}

	@Test
	public void homeProvince() {

		ProvinceDTO prov1 = provServ.getProvince(Double.toString(lat2),
				Double.toString(long2), sid);

		assertEquals("Province latitude", lat2, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", long2, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.HOME, prov1.getType());
		assertEquals("Province name", "lzpD6mFm44", prov1.getProvinceName());
		assertEquals("Province unit size", 10, prov1.getUnitSize());
		assertEquals("Province owner name", "Mr. TK", prov1.getOwnerName());

	}

	@Test
	public void otherPlayerProvince() {
		ProvinceDTO prov1 = provServ.getProvince(Double.toString(-40.4225),
				Double.toString(144.963), "UJ86IpW5xK8ZZH7t"); // JohnnyZQ

		assertEquals("Province latitude", -40.4225, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", 144.963, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.OTHER_PLAYER,
				prov1.getType());
		assertEquals("Province name", "Kvukx9SCOB", prov1.getProvinceName());
		assertEquals("Province unit size", 9, prov1.getUnitSize());
		assertEquals("Province new units", 0, prov1.getNewUnitSize());
		assertEquals("Province owner name", "Mr. TK", prov1.getOwnerName());
		assertEquals("Attackable", true, prov1.isAttackable());
		assertEquals("Attackable", false, prov1.isUnderAttack());
	}

	@Test
	public void ownedProvince() {
		ProvinceDTO prov1 = provServ.getProvince(Double.toString(-40.4225),
				Double.toString(144.963), sid);

		assertEquals("Province latitude", -40.4225, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", 144.963, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.PLAYER, prov1.getType());
		assertEquals("Province name", "Kvukx9SCOB", prov1.getProvinceName());
		assertEquals("Province unit size", 9, prov1.getUnitSize());
		// assertEquals("Province new units", 0, prov1.getNewUnitSize());
		assertEquals("Province owner name", "Mr. TK", prov1.getOwnerName());
		assertEquals("Attackable", false, prov1.isAttackable());
		assertEquals("Under attack", false, prov1.isUnderAttack());
	}

	@Test
	public void ownedProvinceWithoutSid() {
		ProvinceDTO prov1 = provServ.getProvince(Double.toString(-40.4225),
				Double.toString(144.963), cookie);

		assertEquals("Province latitude", -40.4225, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", 144.963, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.OTHER_PLAYER,
				prov1.getType());
		assertEquals("Province name", "Kvukx9SCOB", prov1.getProvinceName());
		assertEquals("Province unit size", 9, prov1.getUnitSize());
		// assertEquals("Province new units", 0, prov1.getNewUnitSize());
		assertEquals("Province owner name", "Mr. TK", prov1.getOwnerName());
		assertEquals("Attackable", true, prov1.isAttackable());
		assertEquals("Under attack", false, prov1.isUnderAttack());
	}

	@Test
	public void otherPlayerHome() {
		double lat3 = 26.1235;
		double long3 = 58.123;
		Province home = new Province(lat3, long3);
		Player player = new Player("Smaug", GeneratorUtil.generateString(16),
				home);

		assertNull(playerRepo.findByUserName("Smaug"));
		provRepo.save(home);
		homeRepo.save(player.getHome());
		playerRepo.save(player);

		ProvinceDTO prov1 = provServ.getProvince(Double.toString(lat3),
				Double.toString(long3), sid);

		assertEquals("Province latitude", lat3, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", long3, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.BOT, prov1.getType());

		assertEquals("Province new units", 0, prov1.getNewUnitSize());
		assertEquals("Province owner name", Constants.BOT_NAME,
				prov1.getOwnerName());
		assertEquals("Attackable", true, prov1.isAttackable());
		assertEquals("Under attack", false, prov1.isUnderAttack());
	}

	@Test
	public void botOwnedProvinceInDatabase() {
		Province prov = new Province(lat1, long1);
		provRepo.save(prov);
		Unit botUnit = new Unit(45);
		unitRepo.save(botUnit);
		Ownership ow = new Ownership(prov, botUnit);
		ownerRepo.save(ow);
		Player bot = playerRepo.findOne(0);
		bot.addOwnership(ow);
		playerRepo.save(bot);

		ProvinceDTO prov1 = provServ.getProvince(Double.toString(lat1),
				Double.toString(long1), sid);

		assertEquals("Province latitude", lat1, prov1.getLatitude(), 0.0001);
		assertEquals("Province longitude", long1, prov1.getLongitude(), 0.001);
		assertEquals("Province type", ProvinceType.BOT, prov1.getType());
		assertEquals("Province unit size", 45, prov1.getUnitSize());

	}

	@Test(expected = NumberFormatException.class)
	public void badParameterTest() {
		provServ.getProvince("dfdf", "sdsd", sid);

	}

}
