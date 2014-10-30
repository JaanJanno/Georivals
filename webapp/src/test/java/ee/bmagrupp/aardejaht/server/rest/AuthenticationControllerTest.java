package ee.bmagrupp.aardejaht.server.rest;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import ee.bmagrupp.aardejaht.server.service.AuthenticationService;
import ee.bmagrupp.aardejaht.server.util.ServerResult;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
		TransactionalTestExecutionListener.class })
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class AuthenticationControllerTest {

	private MockMvc mockMvc;
	private RegistrationResponse goodResponse;

	@InjectMocks
	RegistrationController regCon;

	@Mock
	AuthenticationService authServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(regCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		goodResponse = new RegistrationResponse(ServerResult.OK);

	}

	@Test
	public void succesfulPhase1() throws Exception {
		RegistrationDTO reg = new RegistrationDTO();
		reg.setUserName("new user");

		when(authServ.registrationPhase1(any(RegistrationDTO.class)))
				.thenReturn(goodResponse);

		mockMvc.perform(
				post("/registration/phase1").content(reg.toJson())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.value", is((Object) null)))
				.andExpect(jsonPath("$.result", is("OK")));
	}

}
