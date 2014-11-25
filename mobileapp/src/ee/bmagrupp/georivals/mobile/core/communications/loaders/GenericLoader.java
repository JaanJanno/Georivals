package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ee.bmagrupp.georivals.mobile.core.communications.Connection;
import ee.bmagrupp.georivals.mobile.core.communications.GsonParser;

/**
 * Class for making a HTTP get request to the server and retrieving a generic
 * object parsed from JSON. To define the class of the object to be retrieved
 * put its class name in the <> brackets and pass its type/class as the first
 * argument to the constructor. Use this by overriding the handleResponse()
 * method and calling retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

abstract public class GenericLoader<T> implements Runnable {

	private Class<T> typeClass;
	private Type typeToken;

	protected String url; // URL of the connection destination.
	protected String cookie; // Cookie string;
	private Map<String, String> parameters = new HashMap<String, String>();

	private String requestMethod;

	/**
	 * 
	 * @param typeToken
	 *            Type of the object to be received.
	 * @param url
	 *            Url of the request.
	 */

	public GenericLoader(Type typeToken, String url) {
		this(typeToken, url, "");
	}

	/**
	 * 
	 * @param typeToken
	 *            Type of the object to be received.
	 * @param url
	 *            Url of the request.
	 * @param cookie
	 *            Cookie string sent along with the request.
	 */

	public GenericLoader(Type typeToken, String url, String cookie) {
		this.url = url;
		this.cookie = cookie;
		this.typeToken = typeToken;
	}

	/**
	 * 
	 * @param typeParameterClass
	 *            Class of the object to be received.
	 * @param url
	 *            Url of the request.
	 */

	public GenericLoader(Class<T> typeClass, String url) {
		this(typeClass, url, "");
	}

	/**
	 * 
	 * @param typeParameterClass
	 *            Class of the object to be received.
	 * @param url
	 *            Url of the request.
	 * @param cookie
	 *            Cookie string sent along with the request.
	 */

	public GenericLoader(Class<T> typeClass, String url, String cookie) {
		this.url = url;
		this.cookie = cookie;
		this.typeClass = typeClass;
	}

	/**
	 * Call this method to start a new thread that retrieves information from
	 * given URL and then calls the overridden handleResponseList() method with
	 * the list as argument.
	 */

	public void retrieveResponse() {
		new Thread(this).start();
	}

	/*
	 * Parses a JSON string and returns a generic object of class T. Uses the
	 * type field.
	 */

	protected T getObjectFromJSON(String json) {
		if (typeClass != null)
			return GsonParser.getInstance().fromJson(json, typeClass);
		else
			return GsonParser.getInstance().fromJson(json, typeToken);
	}

	/**
	 * Method called by separate thread. Call retrieveObject() instead of this!
	 */

	@Override
	public void run() {
		Connection c = createConnection();
		handleParameters(c);
		c.sendRequest();
	}

	/*
	 * Creates a new connection.
	 */

	protected Connection createConnection() {
		return new Connection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				T object = getObjectFromJSON(response);
				handleResponse(object);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}
		};
	}

	/**
	 * 
	 * @param requestMethod
	 *            Set HTTP request method.
	 */

	protected void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * Add parameter that will be sent on to the Connection object.
	 * 
	 * @param key
	 * @param value
	 */

	protected void addParameter(String key, String value) {
		parameters.put(key, value);
	}

	/*
	 * Adds all parameters to the Connection object that's about to be created.
	 */

	protected void handleParameters(Connection c) {
		for (String key : parameters.keySet()) {
			c.addParameter(key, parameters.get(key));
		}
		if (requestMethod != null)
			c.setRequestMethod(requestMethod);
	}

	/**
	 * Override this method to define the behavior after an object has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	abstract public void handleResponse(T response);
}