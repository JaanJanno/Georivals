package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.lang.reflect.Type;
import java.util.List;
import ee.bmagrupp.georivals.mobile.core.communications.Connection;
import ee.bmagrupp.georivals.mobile.core.communications.GsonParser;

/**
 * Class for making a HTTP get request to the server and retrieving a generic
 * list parsed from JSON. To define the class of the object in list to be
 * retrieved put its class name in the <> brackets and pass its class as the
 * first argument to the constructor. Use this by overriding the
 * handleResponseList() method and calling retrieveList() method.
 * 
 * @author Jaan Janno
 */

abstract public class GenericListLoader<T> implements Runnable {

	final protected Type typeToken;

	protected String url; // URL of the connection destination.
	protected String cookie = ""; // Cookie string;

	/**
	 * 
	 * @param typeParameterClass
	 *            Class of the object to be received.
	 * @param url
	 */

	public GenericListLoader(Type typeToken, String url) {
		this.url = url;
		this.typeToken = typeToken;
	}

	/**
	 * 
	 * @param typeParameterClass
	 *            Class of the object to be received.
	 * @param url
	 */

	public GenericListLoader(Type typeToken, String url, String cookie) {
		this.url = url;
		this.cookie = cookie;
		this.typeToken = typeToken;
	}

	/**
	 * Call this method to start a new thread that retrieves information from
	 * given URL and then calls the overridden handleResponseList() method with
	 * the list as argument.
	 */

	public void retrieveList() {
		new Thread(this).start();
	}

	/*
	 * Parses a JSON string and returns a generic object of class T.
	 */

	protected List<T> getObjectFromJSON(String json) {
		return GsonParser.getInstance().fromJson(json, typeToken);
	}

	/**
	 * Method called by separate thread. Call retrieveObject() instead of this!
	 */

	@Override
	public void run() {
		Connection c = new Connection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				List<T> object = getObjectFromJSON(response);
				handleResponseList(object);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}

		};
		c.sendRequest();
	}

	/**
	 * Override this method to define the behavior after an object has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	abstract public void handleResponseList(List<T> responseList);

}