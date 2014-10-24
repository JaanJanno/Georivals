package ee.bmagrupp.aardejaht.core.communications.highscore;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import ee.bmagrupp.aardejaht.core.communications.Connection;
import ee.bmagrupp.aardejaht.models.ProfileEntry;

/**
 * @author	Jaan Janno
 */

/**
 * Class for making a HTTP get request to the server and retrieving ProfileEntry data
 * parsed from JSON to objects.
 * Use this by overriding the handleResponseList() method and calling 
 * retrieveProfileEntry() method.
 */

abstract public class ProfileEntryLoader implements Runnable {

	private String url; // URL of the connection destination.

	public ProfileEntryLoader(String url, int index) {
		this.url = url + index;
	}
	
	/**
	 * Call this method to start a new thread that
	 * retrieves information from given URL and
	 * then calls the overridden handleResponseList() method
	 * with the list as argument.
	 */

	public void retrieveProfileEntry() {
		new Thread(this).start();
	}
	
	/*
	 * Parses a JSON string and returns a ProfileEntry.
	 */

	private static ProfileEntry getObjectFromJSON(String json) {
		return new Gson().fromJson(json, ProfileEntry.class);
	}
	
	/**
	 * Method called by separate thread. Call retrieveProfileEntry() instead of this!
	 */

	@Override
	public void run() {
		Map<String, String> parameters = new HashMap<String, String>();
		addRequestParameters(parameters);
		Connection c = new Connection(url, "GET", parameters);
		c.sendRequest();
		try {
			c.join();
			ProfileEntry object = getObjectFromJSON(c.getResponse());
			handleResponseObject(object);
		} catch (InterruptedException | NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Override this method to add parameters for sending
	 * to the server along with the request.
	 */
	
	abstract public void addRequestParameters(Map<String, String> parameters);
	
	/**
	 * Override this method to define the behavior
	 * after an object has been retrieved.
	 * Remember this method doesn't run on the UI thread!
	 */
	
	abstract public void handleResponseObject(ProfileEntry profile);
}