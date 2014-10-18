package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.rest.domain.HighScoreEntry;

public interface HighScoreService {

	HighScoreEntry findById(int id);

	List<HighScoreEntry> findAll();
}
