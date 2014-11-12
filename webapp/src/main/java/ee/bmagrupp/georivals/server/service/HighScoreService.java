package ee.bmagrupp.georivals.server.service;

import java.util.List;

import ee.bmagrupp.georivals.server.rest.domain.HighScoreEntry;

public interface HighScoreService {

	HighScoreEntry findById(int id);

	List<HighScoreEntry> findAll();
}
