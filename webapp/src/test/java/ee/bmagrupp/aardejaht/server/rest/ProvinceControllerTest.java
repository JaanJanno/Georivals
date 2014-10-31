package ee.bmagrupp.aardejaht.server.rest;

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
import ee.bmagrupp.aardejaht.server.rest.domain.CameraFOV;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

/**
 * Sample controller test. Currently this is not worth much, but it took a LONG
 * time to get this far. Will be improved.
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
public class ProvinceControllerTest {

	private MockMvc mockMvc;
	private CameraFOV fov;
	private Cookie cookie;
	private List<ProvinceDTO> provList;

	@InjectMocks
	ProvinceController provCon;

	@Mock
	ProvinceService provServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(provCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		fov = new CameraFOV(2, 3, 4, 5);
		cookie = new Cookie("sid", "HDpVys");

		provList = new ArrayList<ProvinceDTO>();
		provList.add(new ProvinceDTO(1, 2, 3, 100, 12, "bla", 3));

	}

	@Test
	public void fovPost() throws Exception {
		when(provServ.getProvinces(any(CameraFOV.class), anyString()))
				.thenReturn(provList);
		mockMvc.perform(
				post("/province").content(fov.toJson()).cookie(cookie)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isAccepted());
	}

}
