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
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceType;
import ee.bmagrupp.aardejaht.server.rest.domain.ProvinceViewDTO;
import ee.bmagrupp.aardejaht.server.service.ProvinceService;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 *
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
		cookie = new Cookie("sid", "BPUYYOU62flwiWJe");

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
						.accept(MediaType.APPLICATION_JSON)).andExpect(
				status().isAccepted());
	}

	@Test
	public void getProvinceTest() throws Exception {
		double latitude = -40.4195;
		double longitude = 144.961;
		ProvinceViewDTO prov = new ProvinceViewDTO(latitude, longitude,
				ProvinceType.PLAYER, "haha", "Oleg Tartust", true, false, 10, 3);
		when(
				provServ.getProvince(Double.toString(latitude),
						Double.toString(longitude), "BPUYYOU62flwiWJe"))
				.thenReturn(prov);
		mockMvc.perform(
				get("/province").param("latitude", Double.toString(latitude))
						.param("longitude", Double.toString(longitude))
						.cookie(cookie)

						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude", is(latitude)))
				.andExpect(jsonPath("$.longitude", is(longitude)))
				.andExpect(
						jsonPath("$.type", is(ProvinceType.PLAYER.toString())))
				.andExpect(jsonPath("$.provinceName", is("haha")))
				.andExpect(jsonPath("$.ownerName", is("Oleg Tartust")))
				.andExpect(jsonPath("$.underAttack", is(false)))
				.andExpect(jsonPath("$.unitCount", is(10)))
				.andExpect(jsonPath("$.newUnitCount", is(3)))
				.andExpect(jsonPath("$.attackable", is(true)));
	}
}
