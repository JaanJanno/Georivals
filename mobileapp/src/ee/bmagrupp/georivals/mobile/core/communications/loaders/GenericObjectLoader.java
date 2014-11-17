package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ee.bmagrupp.georivals.mobile.core.communications.Connection;
import ee.bmagrupp.georivals.mobile.core.communications.GsonParser;

/**
 * Class for making a HTTP get request to the server and retrieving a
 * generic object parsed from JSON.
 * To define the class of the object to be retrieved put its class name in the <>
 * brackets and pass its class as the first argument to the constructor.
 * Use this by overriding the handleResponseObject() method and calling 
 * retrieveObject() method.
 * @author	Jaan Janno
 */

abstract public class GenericObjectLoader<T> implements Runnable {
	
	final Class<T> typeParameterClass;

	protected String url; 		// URL of the connection destination.
	protected String cookie = "";	// Cookie string;
	private Map<String, String> parameters = new HashMap<String, String>();
	
	private String requestMethod;
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param url
	 */

	public GenericObjectLoader(Class<T> typeParameterClass, String url) {
		this.url = url;
		this.typeParameterClass = typeParameterClass;
	}
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param url
	 */
	
	public GenericObjectLoader(Class<T> typeParameterClass, String url, String cookie) {
		this.url = url;
		this.cookie = cookie;
		this.typeParameterClass = typeParameterClass;
	}
	
	/**
	 * Call this method to start a new thread that
	 * retrieves information from given URL and
	 * then calls the overridden handleResponseList() method
	 * with the list as argument.
	 */

	public void retrieveObject() {
		new Thread(this).start();
	}
	
	/*
	 * Parses a JSON string and returns a generic object of class T.
	 */

	protected T getObjectFromJSON(String json) {
		return (T) GsonParser.getInstance().fromJson(json, typeParameterClass);
	}
	
	/**
	 * Method called by separate thread. Call retrieveObject() instead of this!
	 */

	@Override
	public void run() {
		Connection c = new Connection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				T object = getObjectFromJSON(response);
				handleResponseObject(object);			
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}
		};
		handleParameters(c);
		c.sendRequest();
	}
	
	/**
	 * 
	 * @param requestMethod Set HTTP request method.
	 */
	
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	/**
	 * Add parameter that will be sent on to
	 * the Connection object.
	 * @param key
	 * @param value
	 */
	
	protected void addParamter(String key, String value) {
		parameters.put(key, value);
	}
	
	/*
	 * Adds all parameters to the Connection object
	 * that's about to be created.
	 */
	
	private void handleParameters(Connection c) {
		for (String key: parameters.keySet()){
			c.addParameter(key, parameters.get(key));
		}
		if (requestMethod != null)
			c.setRequestMethod(requestMethod);
	}

	/**
	 * Override this method to define the behavior
	 * after an object has been retrieved.
	 * Remember this method doesn't run on the UI thread!
	 */
	
	abstract public void handleResponseObject(T responseObject);
}