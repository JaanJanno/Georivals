package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import com.google.gson.Gson;

import ee.bmagrupp.georivals.mobile.core.communications.PostConnection;

/**
 * Class for making a HTTP post request to the server and retrieving a generic
 * object parsed from JSON. To define the class of the object to be retrieved
 * put its class name in the <> brackets and pass its class as the first
 * argument to the constructor. Use this by overriding the
 * handleResponseObject() method and calling retrieveObject() method.
 * 
 * @author Jaan Janno
 */

public abstract class GenericObjectPostLoader<T> extends GenericObjectLoader<T> {

	Object post; // Object sent to the server.

	/**
	 * 
	 * @param typeParameterClass
	 * @param url
	 */

	public GenericObjectPostLoader(Class<T> typeParameterClass, String url) {
		super(typeParameterClass, url, "");
	}

	/**
	 * 
	 * @param typeParameterClass
	 *            Type of the response.
	 * @param post
	 *            The object to be posted to the server.
	 * @param url
	 *            Url of the request.
	 */

	public GenericObjectPostLoader(Class<T> typeParameterClass, Object post,
			String url) {
		super(typeParameterClass, url, "");
		this.post = post;
	}

	/**
	 * 
	 * @param typeParameterClass
	 *            Type of the response.
	 * @param post
	 *            The object to be posted to the server.
	 * @param url
	 *            Url of the request.
	 * @param cookie
	 */

	public GenericObjectPostLoader(Class<T> typeParameterClass, Object post,
			String url, String cookie) {
		super(typeParameterClass, url, cookie);
		this.post = post;
	}

	/**
	 * Method called by separate thread. Call retrieveObject() instead of this!
	 */

	@Override
	public void run() {
		PostConnection c = new PostConnection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				T object = getObjectFromJSON(response);
				handleResponseObject(object);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}

			@Override
			public void writeToConnection(DataOutputStream writer)
					throws IOException {
				if (post != null) {
					String json = new Gson().toJson(post);
					writer.writeBytes(json);
				}
			}
		};
		handleParameters(c);
		c.sendRequest();
	}

	/**
	 * Override this method to define the behavior after an object has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	@Override
	abstract public void handleResponseObject(T responseObject);

}
