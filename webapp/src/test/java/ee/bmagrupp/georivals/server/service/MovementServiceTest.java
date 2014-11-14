package ee.bmagrupp.georivals.server.service;

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

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;

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

	@Test
	public void getMyUnitsTest() {
		List<MovementSelectionViewDTO> dtos = movServ
				.getMyUnits("BPUYYOU62flwiWJe"); // Mr.TK

		assertEquals("The number of units this player has", 2, dtos.size());

		assertEquals("Unit type", ProvinceType.PLAYER, dtos.get(0).getType());
		assertEquals("Unit size", 9, dtos.get(0).getUnitSize());
		assertEquals("Unit id", 6, dtos.get(0).getUnitId());
		assertEquals("Unit province name","Kvukx9SCOB", dtos.get(0).getProvinceName());

		// Home province
		assertEquals("Unit size", 10, dtos.get(1).getUnitSize());
		assertEquals("Unit province name", "lzpD6mFm44", dtos.get(1)
				.getProvinceName());
		assertEquals("Unit unit id", 7, dtos.get(1).getUnitId());
		assertEquals("Unit type", ProvinceType.HOME, dtos.get(1).getType());
	}

}
