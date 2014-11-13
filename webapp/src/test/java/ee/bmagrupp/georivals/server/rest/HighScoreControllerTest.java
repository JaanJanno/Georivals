package ee.bmagrupp.georivals.server.rest;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


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
import ee.bmagrupp.georivals.server.rest.HighScoreController;
import ee.bmagrupp.georivals.server.rest.domain.HighScoreEntry;
import ee.bmagrupp.georivals.server.service.HighScoreService;

/**
 * Tests for {@link HighScoreController}. Everything is tested.
 * 
 * @author TKasekamp
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class HighScoreControllerTest {

	private MockMvc mockMvc;
	private List<HighScoreEntry> highScoreList;
	private HighScoreEntry entry;

	@InjectMocks
	HighScoreController highScoreCon;

	@Mock
	HighScoreService highScoreServ;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);

		mockMvc = MockMvcBuilders
				.standaloneSetup(highScoreCon)
				.setMessageConverters(new MappingJackson2HttpMessageConverter())
				.build();

		entry = new HighScoreEntry(1, "A", 2.0, 3);
		highScoreList = new ArrayList<HighScoreEntry>();
		highScoreList.add(entry);
		highScoreList.add(new HighScoreEntry(2, "B", 1.0, 5));

	}

	@Test
	public void allHighScoresTestSuccess() throws Exception {
		when(highScoreServ.findAll()).thenReturn(highScoreList);

		mockMvc.perform(get("/highscore").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].id", is(1)))
				.andExpect(jsonPath("$.[0].username", is("A")))
				.andExpect(jsonPath("$.[0].averageUnits", is(2.0)))
				.andExpect(jsonPath("$.[0].provincesOwned", is(3)))
				.andExpect(jsonPath("$.[1].id", is(2)))
				.andExpect(jsonPath("$.[1].username", is("B")))
				.andExpect(jsonPath("$.[1].averageUnits", is(1.0)))
				.andExpect(jsonPath("$.[1].provincesOwned", is(5)));
	}

	@Test
	public void allHighScoresTestFail() throws Exception {
		when(highScoreServ.findAll()).thenReturn(null);

		mockMvc.perform(get("/highscore").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

	@Test
	public void specificHighScoreTestSuccess() throws Exception {
		when(highScoreServ.findById(1)).thenReturn(entry);

		mockMvc.perform(get("/highscore/1").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.username", is("A")))
				.andExpect(jsonPath("$.averageUnits", is(2.0)))
				.andExpect(jsonPath("$.provincesOwned", is(3)));
	}

	@Test
	public void specificHighScoreTestFail() throws Exception {
		when(highScoreServ.findById(20)).thenReturn(null);

		mockMvc.perform(get("/highscore/20").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().string(""));
	}

}
