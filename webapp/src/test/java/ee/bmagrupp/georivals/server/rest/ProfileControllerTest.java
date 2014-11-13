package ee.bmagrupp.georivals.server.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import ee.bmagrupp.georivals.server.rest.ProfileController;
import ee.bmagrupp.georivals.server.rest.domain.PlayerProfile;
import ee.bmagrupp.georivals.server.service.ProfileService;

/**
 * Tests for {@link ProfileController}. All cases have been covered.
 * 
 * @author TKasekamp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
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
						MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	public void profilePathSuccess() throws Exception {
		when(profServ.getPlayerProfile(1)).thenReturn(prof);

		mockMvc.perform(get("/profile/id/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("Mr. TK")))
				.andExpect(jsonPath("$.email", is("mr.tk@pacific.ee")))
				.andExpect(jsonPath("$.totalUnits", is(23)))
				.andExpect(jsonPath("$.ownedProvinces", is(2)));
	}

	@Test
	public void profilePathFail() throws Exception {
		when(profServ.getPlayerProfile(100)).thenReturn(null);

		mockMvc.perform(get("/profile/id/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

}
