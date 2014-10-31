package ee.bmagrupp.aardejaht.server.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;
import ee.bmagrupp.aardejaht.server.service.ProfileService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class ProfileControllerTest {

	private MockMvc mockMvc;
	private Cookie goodCookie;
	private Cookie badCookie;
	private PlayerProfile prof;

	@InjectMocks
	ProfileController profCon;

	@Mock
	ProfileService profServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(profCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		goodCookie = new Cookie("sid", "BPUYYOU62flwiWJe");
		badCookie = new Cookie("sid", "BPUYYOU62flwiWJ213e");

		prof = new PlayerProfile(1, "Mr. TK", "mr.tk@pacific.ee", 23, 2);
	}

	@Test
	public void profileSidSuccess() throws Exception {
		when(profServ.getPlayerProfile("BPUYYOU62flwiWJe")).thenReturn(prof);

		mockMvc.perform(
				get("/profile").cookie(goodCookie).accept(
						MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("Mr. TK")))
				.andExpect(jsonPath("$.email", is("mr.tk@pacific.ee")))
				.andExpect(jsonPath("$.totalUnits", is(23)))
				.andExpect(jsonPath("$.ownedProvinces", is(2)));
	}

	@Test
	public void profileSidFail() throws Exception {
		when(profServ.getPlayerProfile("BPUYYOU62flwiWJ213e")).thenReturn(null);

		mockMvc.perform(
				get("/profile").cookie(badCookie).accept(
						MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isBadRequest());
	}

}
