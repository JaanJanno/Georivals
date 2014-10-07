package ee.bmagrupp.aardejaht.core.communications.highscore;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ee.bmagrupp.aardejaht.core.communications.Connection;
import ee.bmagrupp.aardejaht.core.communications.exceptions.IncompleteRequestException;
import ee.bmagrupp.aardejaht.models.HighScoreEntry;

public class HighScoreListLoader implements Runnable {

	private Thread thread;
	private List<HighScoreEntry> entries;
	private String url;

	public HighScoreListLoader(String url) {
		thread = new Thread(this);
		this.url = url;
	}

	void startRetrieveHighScoreEntries() {
		thread.start();
	}

	public List<HighScoreEntry> getEntries() {
		if (entries == null)
			throw new IncompleteRequestException("Request not completed.");
		return entries;
	}

	List<HighScoreEntry> getListFromJSON(String json) {
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
		entries = getListFromJSON(c.getResponse());
	}
	
	public void join() throws InterruptedException {
		thread.join();
	}
}
