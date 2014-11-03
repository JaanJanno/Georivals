package ee.bmagrupp.aardejaht.core.communications.loaders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ee.bmagrupp.aardejaht.core.communications.PostConnection;

/**
 * Class for making a HTTP get request to the server and retrieving a
 * generic list parsed from JSON.
 * To define the class of the object in list to be retrieved put its class name in the <>
 * brackets and pass its class as the first argument to the constructor.
 * Use this by overriding the handleResponseList() method and calling 
 * retrieveList() method.
 * @author	Jaan Janno
 */

abstract public class GenericListPostLoader<T> implements Runnable {
	
	final Class<T> typeParameterClass;

	private String url; 		// URL of the connection destination.
	protected String cookie = "";	// Cookie string;
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param url
	 */

	public GenericListPostLoader(Class<T> typeParameterClass, String url) {
		this.url = url;
		this.typeParameterClass = typeParameterClass;
	}
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param url
	 */
	
	public GenericListPostLoader(Class<T> typeParameterClass, String url, String cookie) {
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

	public void retrieveList() {
		new Thread(this).start();
	}
	
	/*
	 * Parses a JSON string and returns a generic object of class T.
	 */

	protected List<T> getObjectFromJSON(String json) {
		Type listType = new TypeToken<ArrayList<T>>() {
		}.getType();
		return new Gson().fromJson(json, listType);
	}
	
	/**
	 * Method called by separate thread. Call retrieveObject() instead of this!
	 */

	@Override
	public void run() {
		PostConnection c = new PostConnection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				List<T> object = getObjectFromJSON(response);
				handleResponseList(object);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}

			@Override
			public void writeToConnection(DataOutputStream writer)
					throws IOException {
				writeRequestBody(writer);
			}
		};
		c.sendRequest();
	}
	
	/**
	 * Override this method to define the behavior
	 * after an object has been retrieved.
	 * Remember this method doesn't run on the UI thread!
	 */
	
	abstract public void handleResponseList(List<T> responseList);
	
	abstract public void writeRequestBody(DataOutputStream writer) throws IOException;
}