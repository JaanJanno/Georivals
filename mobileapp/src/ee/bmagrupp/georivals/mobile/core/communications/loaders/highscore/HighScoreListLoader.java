package ee.bmagrupp.georivals.mobile.core.communications.loaders.highscore;

import java.util.List;

import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericLoader;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;

/**
 * Class for retrieving HighScore info from server. Use this by overriding the
 * handleResponse() method and calling it.
 * 
 * @author Jaan Janno
 */

public abstract class HighScoreListLoader extends
		GenericLoader<List<HighScoreEntry>> {

	public HighScoreListLoader() {
		super(HighScoreEntry.listType, Constants.HIGHSCORE);
	}

}