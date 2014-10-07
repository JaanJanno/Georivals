package ee.bmagrupp.aardejaht.core.communications.highscore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ee.bmagrupp.aardejaht.core.communications.Connection;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;

abstract class HighScoreListLoader implements Runnable {

	private String url;

	public HighScoreListLoader(String url) {
		this.url = url;
	}

	void retrieveHighScoreEntries() {
		new Thread(this).start();
	}

	private static List<HighScoreEntry> getListFromJSON(String json) {
		Type listType = new TypeToken<ArrayList<HighScoreEntry>>() {
		}.getType();
		return new Gson().fromJson(json, listType);
	}

	@Override
	public void run() {
		Connection c = new Connection(url);
		c.sendRequest();
		try {
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<HighScoreEntry> list = getListFromJSON(c.getResponse());
		handleResponseList(list);
	}
	
	abstract void handleResponseList(List<HighScoreEntry> list);
}
