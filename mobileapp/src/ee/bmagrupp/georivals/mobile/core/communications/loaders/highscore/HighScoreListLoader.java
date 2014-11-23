package ee.bmagrupp.georivals.mobile.core.communications.loaders.highscore;

import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Constants;
import ee.bmagrupp.georivals.mobile.core.communications.loaders.GenericListLoader;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;

/**
 * Class for retrieving HighScore info from server. Use this by overriding the
 * handleResponseList() method and calling it.
 * 
 * @author Jaan Janno
 */

public abstract class HighScoreListLoader extends
		GenericListLoader<HighScoreEntry> {

	public HighScoreListLoader() {
		super(HighScoreEntry.listType, Constants.HIGHSCORE);
	}

	/**
	 * Override this method to define the behavior after a list has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	public abstract void handleResponseList(List<HighScoreEntry> responseList);

}