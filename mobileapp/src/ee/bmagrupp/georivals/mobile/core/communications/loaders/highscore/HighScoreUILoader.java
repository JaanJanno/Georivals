package ee.bmagrupp.georivals.mobile.core.communications.loaders.highscore;

import java.util.List;

import android.app.Activity;

import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoadable;
import ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface.UILoader;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;

/**
 * Class for loading a list of high score entries entry from server and handling
 * them on UI.<br>
 * 
 * Override the response handling methods to use it. Call retrieveResponse to
 * send the query to server.
 * 
 * @author Jaan Janno
 * 
 */

public abstract class HighScoreUILoader extends HighScoreListLoader implements
		UILoadable<List<HighScoreEntry>> {

	Activity activity;

	/**
	 * 
	 * @param activity
	 *            Android activity that is modified.
	 */

	public HighScoreUILoader(Activity activity) {
		this.activity = activity;
	}

	/**
	 * Handles response on both UI and in background if so defined. Usually
	 * called by the object itself after getting a response. Useless to call
	 * manually.
	 */

	@Override
	public void handleResponse(List<HighScoreEntry> response) {
		UILoader.load(response, this, activity);
	}

}
