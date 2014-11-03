package ee.bmagrupp.aardejaht.server.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationDTO;
import ee.bmagrupp.aardejaht.server.rest.domain.RegistrationResponse;
import ee.bmagrupp.aardejaht.server.service.RegistrationService;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

/**
 * Tests for {@link RegistrationController} by mocking the hell out of
 * {@link RegistrationService}.
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
public class RegistrationControllerTest {

	private MockMvc mockMvc;
	private RegistrationResponse goodResponse;
	private RegistrationResponse userNameInUse;

	private RegistrationDTO reg1;
	private RegistrationDTO reg2;

	@InjectMocks
	RegistrationController regCon;

	@Mock
	RegistrationService authServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(regCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		goodResponse = new RegistrationResponse(ServerResult.OK);
		userNameInUse = new RegistrationResponse(ServerResult.USERNAME_IN_USE);
		reg1 = new RegistrationDTO();
		reg1.setUserName("Smaug");

		reg2 = new RegistrationDTO();
		reg2.setUserName("Smaug");
		reg2.setHomeLat(123);
		reg2.setHomeLong(123);

	}

	@Test
	public void phase1success() throws Exception {

		when(authServ.registrationPhase1(any(RegistrationDTO.class)))
				.thenReturn(goodResponse);

		mockMvc.perform(
				post("/registration/phase1").content(reg1.toJson())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.value", is((Object) null)))
				.andExpect(jsonPath("$.result", is("OK")))
				.andExpect(jsonPath("$.id", is(0)));
	}

	@Test
	public void phase1UserExists() throws Exception {

		when(authServ.registrationPhase1(any(RegistrationDTO.class)))
				.thenReturn(userNameInUse);

		mockMvc.perform(
				post("/registration/phase1").content(reg1.toJson())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.value", is((Object) null)))
				.andExpect(jsonPath("$.result", is("USERNAME_IN_USE")))
				.andExpect(jsonPath("$.id", is(0)));
	}

	@Test
	public void phase2success() throws Exception {

		RegistrationResponse res = new RegistrationResponse(ServerResult.OK,
				"abcd", 511);
		when(authServ.registrationPhase2(any(RegistrationDTO.class)))
				.thenReturn(res);

		mockMvc.perform(
				post("/registration/phase2").content(reg2.toJson())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.value", is("abcd")))
				.andExpect(jsonPath("$.result", is("OK")))
				.andExpect(jsonPath("$.id", is(511)));
	}

	@Test
	public void phase2userExists() throws Exception {

		when(authServ.registrationPhase2(any(RegistrationDTO.class)))
				.thenReturn(userNameInUse);

		mockMvc.perform(
				post("/registration/phase2").content(reg2.toJson())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.value", is((Object) null)))
				.andExpect(jsonPath("$.result", is("USERNAME_IN_USE")))
				.andExpect(jsonPath("$.id", is(0)));
	}

}
