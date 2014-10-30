package ee.bmagrupp.aardejaht.server.core.repository;

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
import ee.bmagrupp.aardejaht.server.core.domain.Province;

/**
 * Tests for {@link ProvinceRepository}
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
public class ProvinceRepositoryTest {

	@Autowired
	ProvinceRepository provRepo;

	@Test
	public void allProvinces() {
		List<Province> provs = (List<Province>) provRepo.findAll();

		assertEquals("There should be 6 Provinces", 6, provs.size());
	}

	@Test
	public void singleProvince() {
		Province prov = provRepo.findOne(1);

		assertEquals("Province id", 1, prov.getId());
	}

	@Test
	public void findProvinceWithLatLongSuccess() {
		Province prov = new Province(58.123, 26.123);
		provRepo.save(prov);
		Province prov2 = provRepo.findWithLatLong(26.123, 58.123);

		assertEquals("Province latitude", 26.123, prov2.getLatitude(), 0.0001);
		assertEquals("Province longitude", 58.123, prov2.getLongitude(), 0.0001);
	}

}
