package ee.bmagrupp.aardejaht.server.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ee.bmagrupp.aardejaht.server.core.old.HighScoreEntry;


public interface HighScoreService extends
		CrudRepository<HighScoreEntry, Long> {

	HighScoreEntry findByUsername(String username);

	HighScoreEntry findById(int id);

	List<HighScoreEntry> findAll();
}
