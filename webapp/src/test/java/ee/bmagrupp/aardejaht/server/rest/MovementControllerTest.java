package ee.bmagrupp.aardejaht.server.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.*;
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
import ee.bmagrupp.aardejaht.server.rest.domain.BeginMovementDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.BeginMovementResponse;
import ee.bmagrupp.aardejaht.server.rest.domain.MovementSelectionViewDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceType;
import ee.bmagrupp.aardejaht.server.service.MovementService;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

/**
 * Test for {@link MovementController}
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
public class MovementControllerTest {

	private MockMvc mockMvc;

	private Cookie cookie;
	private List<MovementSelectionViewDTO> movList;
	private List<BeginMovementDTO> beginMoveList;

	@InjectMocks
	MovementController moveCon;

	@Mock
	MovementService moveServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(moveCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		cookie = new Cookie("sid", "BPUYYOU62flwiWJe");

		movList = new ArrayList<MovementSelectionViewDTO>();
		movList.add(new MovementSelectionViewDTO("Nurk", 18, 34));
		movList.add(new MovementSelectionViewDTO("Kodu", 56, 2,
				ProvinceType.HOME));
		beginMoveList = new ArrayList<BeginMovementDTO>();
		beginMoveList.add(new BeginMovementDTO(34, 45));
		beginMoveList.add(new BeginMovementDTO(35, 2));

	}

	@Test
	public void getMyProvincesTest() throws Exception {
		when(moveServ.getMyUnits("BPUYYOU62flwiWJe")).thenReturn(movList);
		mockMvc.perform(
				get("/movement/myunits").cookie(cookie).accept(
						MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andDo(print())
				.andExpect(jsonPath("$.[0].unitId", is(18)))
				.andExpect(jsonPath("$.[0].provinceName", is("Nurk")))
				.andExpect(jsonPath("$.[0].unitSize", is(34)))
				.andExpect(
						jsonPath("$.[0].type",
								is(ProvinceType.PLAYER.toString())))
				.andExpect(jsonPath("$.[1].unitId", is(56)))
				.andExpect(jsonPath("$.[1].provinceName", is("Kodu")))
				.andExpect(jsonPath("$.[1].unitSize", is(2)))
				.andExpect(
						jsonPath("$.[1].type", is(ProvinceType.HOME.toString())));
	}

	@Test
	public void moveToTest() throws Exception {
		String lat = "23.4565";
		String lon = "83.453";

		Date curDate = new Date();
		String json = "[" + beginMoveList.get(0).toJson() + ","
				+ beginMoveList.get(0).toJson() + "]";

		BeginMovementResponse movRes = new BeginMovementResponse(curDate,
				ServerResult.OK);
		when(
				moveServ.moveUnitsTo(eq(lat), eq(lon),
						anyListOf(BeginMovementDTO.class),
						eq(cookie.getValue()))).thenReturn(movRes);

		mockMvc.perform(
				post("/movement/to").param("latitude", lat)
						.param("longitude", lon).content(json).cookie(cookie)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(ServerResult.OK.toString())))
				.andExpect(jsonPath("$.arrivalTime", is(curDate.getTime())));

	}

}
