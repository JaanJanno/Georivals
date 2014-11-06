package ee.bmagrupp.aardejaht.server.service;

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

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceDTO;

/**
 * Integration tests for {@link ProvinceService}.
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
public class ProvinceServiceIntegrationTest {

	@Autowired
	ProvinceService provServ;

	private CameraFOV fov;
	private int playerID;

	@Before
	public void setUp() {
		// -40.4195, 144.961
		fov = new CameraFOV(-40.423,144.960,-40.419,144.966);
		playerID = 1; // ID for Mr. TK
	}
	
	@Test
	public void noSidTest() {
	    String cookie = "cookie"; // Default sid value
	    List<ProvinceDTO> provList = provServ.getProvinces(fov, cookie);
	 
	    assertEquals("Provinces in area", 12, provList.size());
	    for (ProvinceDTO a : provList) {
	        assertEquals("Nobody should have any new units", 0,a.getNewUnitCount());
	    }
	} 

	@Test
	public void provinceNumberTest() {
		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceDTO> provList = provServ.getProvinces(fov, cookie);

		assertEquals("Provinces in area", 12, provList.size());
	}

	@Test
	public void dataBaseProvinceTest() {

		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceDTO> provList = provServ.getProvinces(fov, cookie);

		for (ProvinceDTO a : provList) {
			if (a.getPlayerId() != playerID) {
				assertEquals("Other players/BOTS should have 0 new units", 0,a.getNewUnitCount());
			}
		}
	}

	@Test
	public void provinceOrderingTest() {
		String cookie = "BPUYYOU62flwiWJe"; // User Mr.TK
		List<ProvinceDTO> provList = provServ.getProvinces(fov, cookie);

		double lastLat = provList.get(0).getLatitude() - 1;
		double lastLong = provList.get(0).getLongitude() - 1;

		for (ProvinceDTO a : provList) {
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
