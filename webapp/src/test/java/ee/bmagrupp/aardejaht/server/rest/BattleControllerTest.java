package ee.bmagrupp.aardejaht.server.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ee.bmagrupp.aardejaht.server.Application;
import ee.bmagrupp.aardejaht.server.rest.domain.BattleHistoryDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.BattleType;
import ee.bmagrupp.aardejaht.server.service.BattleService;

/**
 * Tests for {@link BattleController}
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
public class BattleControllerTest {

	private MockMvc mockMvc;
	private Cookie cookie;
	private List<BattleHistoryDTO> battleList;

	@InjectMocks
	BattleController battleCon;

	@Mock
	BattleService battleServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(battleCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		cookie = new Cookie("sid", "BPUYYOU62flwiWJe");
		battleList = new ArrayList<>();
		battleList.add(new BattleHistoryDTO(54, "Raekoda",
				"Peeter meeter termomeeter", 45, 27, 14, 27,
				BattleType.ATTACK_PLAYER_WON));

	}

	@Test
	public void battleHistoryTest() throws Exception {

		when(battleServ.getBattles(cookie.getValue())).thenReturn(battleList);
		mockMvc.perform(
				get("/battle/history").cookie(cookie).accept(
						MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].battleId", is(54)))
				.andExpect(jsonPath("$.[0].provinceName", is("Raekoda")))
				.andExpect(
						jsonPath("$.[0].otherPlayer",
								is("Peeter meeter termomeeter")))
				.andExpect(jsonPath("$.[0].myUnits", is(45)))
				.andExpect(jsonPath("$.[0].otherUnits", is(27)))
				.andExpect(jsonPath("$.[0].otherUnits", is(27)))
				.andExpect(jsonPath("$.[0].myLosses", is(14)))
				.andExpect(jsonPath("$.[0].otherLosses", is(27)))
				.andExpect(
						jsonPath("$.[0].type",
								is(BattleType.ATTACK_PLAYER_WON.toString())));
	}

}
