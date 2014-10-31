package ee.bmagrupp.aardejaht.server.service;

import java.util.List;

import ee.bmagrupp.aardejaht.server.core.domain.Player;
import ee.bmagrupp.aardejaht.server.rest.domain.PlayerProfile;

public interface ProfileService {

	void createSampleData();

	PlayerProfile getPlayerProfile(int id);

	List<PlayerProfile> findAll();

	/**
	 * Get the {@link PlayerProfile} of the {@link Player} with this sid.
	 * 
	 * @param sid
	 *            Cookie value
	 * @return {@link PlayerProfile} if found. {@code null} if not found.
	 */
	PlayerProfile getPlayerProfile(String sid);
}
