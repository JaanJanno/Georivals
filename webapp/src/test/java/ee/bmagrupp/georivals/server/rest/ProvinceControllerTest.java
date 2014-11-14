package ee.bmagrupp.georivals.server.rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;

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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import ee.bmagrupp.georivals.server.Application;
import ee.bmagrupp.georivals.server.rest.ProvinceController;
import ee.bmagrupp.georivals.server.rest.domain.CameraFOV;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceDTO;
import ee.bmagrupp.georivals.server.rest.domain.ProvinceType;
import ee.bmagrupp.georivals.server.rest.domain.ServerResponse;
import ee.bmagrupp.georivals.server.service.ProvinceService;
import ee.bmagrupp.georivals.server.util.ServerResult;
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
public class ProvinceControllerTest {

	private MockMvc mockMvc;
	private CameraFOV fov;
	private Cookie cookie;
	private List<ProvinceDTO> provList;
	private double latitude;
	private double longitude;
	private ServerResponse response;

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
		provList.add(new ProvinceDTO(2, 3, ProvinceType.PLAYER, "bla", "owner",
				true, false, 100, 3));

		latitude = -40.4195;
		longitude = 144.961;
		response = new ServerResponse(ServerResult.OK);

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

		ProvinceDTO prov = new ProvinceDTO(latitude, longitude,
				ProvinceType.PLAYER, "haha", "Oleg Tartust", true, false, 10, 3);
		when(
				provServ.getProvince(Double.toString(latitude),
						Double.toString(longitude), "BPUYYOU62flwiWJe"))
				.thenReturn(prov);
		mockMvc.perform(
				get("/province").param("latitude", Double.toString(latitude))
						.param("longitude", Double.toString(longitude))
						.cookie(cookie).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.latitude", is(latitude)))
				.andExpect(jsonPath("$.longitude", is(longitude)))
				.andExpect(
						jsonPath("$.type", is(ProvinceType.PLAYER.toString())))
				.andExpect(jsonPath("$.provinceName", is("haha")))
				.andExpect(jsonPath("$.ownerName", is("Oleg Tartust")))
				.andExpect(jsonPath("$.underAttack", is(false)))
				.andExpect(jsonPath("$.unitSize", is(10)))
				.andExpect(jsonPath("$.newUnitSize", is(3)))
				.andExpect(jsonPath("$.attackable", is(true)));
	}

	@Test
	public void getMyProvincesTest() throws Exception {

		List<ProvinceDTO> provs = new ArrayList<ProvinceDTO>();
		provs.add(new ProvinceDTO(latitude, longitude, ProvinceType.PLAYER,
				"haha", "Mr. TK", false, false, 10, 3));
		when(provServ.getMyProvinces("BPUYYOU62flwiWJe")).thenReturn(provs);
		mockMvc.perform(
				get("/province/myprovinces").cookie(cookie).accept(
						MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].latitude", is(latitude)))
				.andExpect(jsonPath("$.[0].longitude", is(longitude)))
				.andExpect(
						jsonPath("$.[0].type",
								is(ProvinceType.PLAYER.toString())))
				.andExpect(jsonPath("$.[0].provinceName", is("haha")))
				.andExpect(jsonPath("$.[0].ownerName", is("Mr. TK")))
				.andExpect(jsonPath("$.[0].underAttack", is(false)))
				.andExpect(jsonPath("$.[0].unitSize", is(10)))
				.andExpect(jsonPath("$.[0].newUnitSize", is(3)))
				.andExpect(jsonPath("$.[0].attackable", is(false)));
	}

	@Test
	public void changeHomeProvinceTest() throws Exception {

		when(
				provServ.changeHomeProvince(Double.toString(latitude),
						Double.toString(longitude), "BPUYYOU62flwiWJe"))
				.thenReturn(response);
		mockMvc.perform(
				post("/province/changehome")
						.param("latitude", Double.toString(latitude))
						.param("longitude", Double.toString(longitude))
						.cookie(cookie).accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(ServerResult.OK.toString())));

	}

	@Test
	public void renameProvinceTest() throws Exception {

		when(
				provServ.renameProvince(Double.toString(latitude),
						Double.toString(longitude), "Mordor",
						"BPUYYOU62flwiWJe")).thenReturn(response);
		mockMvc.perform(
				post("/province/rename")
						.param("latitude", Double.toString(latitude))
						.param("longitude", Double.toString(longitude))
						.param("newname", "Mordor").cookie(cookie)
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result", is(ServerResult.OK.toString())));

	}

}
