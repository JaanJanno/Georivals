package ee.bmagrupp.aardejaht.server.service;

import static org.junit.Assert.*;

import java.util.List;

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

	@Test
	public void provinceTest() {
		CameraFOV fov = new CameraFOV(26.7091679,58.3479039, 26.7598840, 58.3872609
				);
		String cookie = "HDpVys"; // User Mr.TK
		List<ProvinceDTO> provList = provServ.getProvinces(fov, cookie);

		// I don't actually know how many provinces are in that area
		assertEquals("Provinces in area", 1020, provList.size());
	}

}
