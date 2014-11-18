package ee.bmagrupp.georivals.mobile.core.communications.highscore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ee.bmagrupp.georivals.mobile.core.communications.Connection;
import ee.bmagrupp.georivals.mobile.models.highscore.HighScoreEntry;

/**
 * Class for making a HTTP get request to the server and retrieving HighScore
 * data parsed from JSON to objects. Use this by overriding the
 * handleResponseList() method and calling retrieveHighScoreEntries() method.
 * 
 * @author Jaan Janno
 */

abstract public class HighScoreListLoader implements Runnable {

	private String url; // URL of the connection destination.

	public HighScoreListLoader(String url) {
		this.url = url;
	}

	/**
	 * Call this method to start a new thread that retrieves information from
	 * given URL and then calls the overridden handleResponseList() method with
	 * the list as argument.
	 */

	public void retrieveHighScoreEntries() {
		new Thread(this).start();
	}

	/*
	 * Parses a JSON string and returns a list of HighScoreEntries.
	 */

	private static List<HighScoreEntry> getListFromJSON(String json) {
		Type listType = new TypeToken<ArrayList<HighScoreEntry>>() {
		}.getType();
		return new Gson().fromJson(json, listType);
	}

	/**
	 * Method called by separate thread. Call retrieveHighScoreEntries() instead
	 * of this!
	 */

	@Override
	public void run() {
		Connection c = new Connection(url) {

			@Override
			public void handleResponseBody(String response) {
				List<HighScoreEntry> list = getListFromJSON(response);
				handleResponseList(list);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}

		};
		c.sendRequest();
	}

	/**
	 * Override this method to define the behavior after a list has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	abstract public void handleResponseList(List<HighScoreEntry> list);
}
