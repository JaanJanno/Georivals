package ee.bmagrupp.georivals.server.service;

import static org.junit.Assert.*;

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
import ee.bmagrupp.georivals.server.core.domain.Player;
import ee.bmagrupp.georivals.server.core.repository.PlayerRepository;
import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceViewDTO;
import ee.bmagrupp.georivals.server.service.ProvinceService;

/**
 * Integration tests for {@link ProvinceService}. Only focused on generating
 * provinces for the MapView.
 * 
 * @author TKasekamp
 * @author Sander
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ProvinceServiceGeneratorTest {

	@Autowired
	ProvinceService provServ;
	
	@Autowired
	PlayerRepository playerRepo;

	private CameraFOV fov;
	private CameraFOV fov2;
	private int playerID;

	@Before
	public void setUp() {
		fov = new CameraFOV(-40.423, 144.960, -40.419, 144.966);
		fov2 = new CameraFOV(59.4146016, 24.7276848, 59.4194347, 24.7342740);
		playerID = 1; // ID for Mr. TK
	}

	@Test
	public void noSidTest() {
		String cookie = "cookie"; // Default sid value
		List<ProvinceViewDTO> provList = provServ.getProvinces(fov, cookie);

		assertEquals("Provinces in area", 12, provList.size());
		for (ProvinceViewDTO a : provList) {
			assertEquals("Nobody should have any new units", 0,
					a.getNewUnitSize());
		}
	}

	@Test
	public void provinceNumberTest() {
		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceViewDTO> provList = provServ.getProvinces(fov, cookie);

		assertEquals("Provinces in area", 12, provList.size());
	}
	
	@Test
	public void provinceNumberTest2() {
		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		
		List<ProvinceViewDTO> provList = provServ.getProvinces(fov2, cookie);

		assertEquals("Provinces in area", 30, provList.size());
	}

	@Test
	public void dataBaseProvinceTest() {

		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceViewDTO> provList = provServ.getProvinces(fov, cookie);

		for (ProvinceViewDTO a : provList) {
			Player player = playerRepo.findByUserName(a.getOwnerName());
			if (player.getId() != playerID) {
				assertEquals("Other players/BOTS should have 0 new units", 0,
						a.getNewUnitSize());
			}
		}
	}

	@Test
	public void provinceOrderingTest() {
		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceViewDTO> provList = provServ.getProvinces(fov, cookie);

		double lastLat = provList.get(0).getLatitude() - 1;
		double lastLong = provList.get(0).getLongitude() - 1;

		for (ProvinceViewDTO a : provList) {
			double y = a.getLatitude();
			double x = a.getLongitude();
			if (x > lastLong) {
				lastLong = x;
			} else {
				assertTrue(y > lastLat);
				x = lastLong;
				y = lastLat;
			}
		}

	}

}
