package ee.bmagrupp.aardejaht.core.communications.loaders;

import java.util.List;
import com.google.gson.Gson;
import ee.bmagrupp.aardejaht.core.communications.Connection;

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
		return (T) new Gson().fromJson(json, typeParameterClass);
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
		c.sendRequest();
	}
	
	/**
	 * Override this method to define the behavior
	 * after an object has been retrieved.
	 * Remember this method doesn't run on the UI thread!
	 */
	
	abstract public void handleResponseObject(T responseObject);
}