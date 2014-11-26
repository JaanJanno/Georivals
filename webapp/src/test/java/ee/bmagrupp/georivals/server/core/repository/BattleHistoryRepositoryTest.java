package ee.bmagrupp.georivals.server.core.repository;

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
import ee.bmagrupp.georivals.server.core.domain.BattleHistory;

/**
 * Tests for {@link BattleHistoryRepository}
 * 
 * @author TKasekamp
 *
 */
@SpringApplicationConfiguration(classes = Application.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class BattleHistoryRepositoryTest {

	@Autowired
	BattleHistoryRepository batHistRepo;

	@Test
	public void sortByDateTest() {
		List<BattleHistory> battles = batHistRepo
				.findBySidSortByDate("BPUYYOU62flwiWJe");
		assertEquals("2 battles", 2, battles.size());
		assertTrue("Newer battle should be first", battles.get(0).getDate()
				.after(battles.get(1).getDate()));
	}
}
