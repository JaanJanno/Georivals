package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ee.bmagrupp.georivals.mobile.core.communications.PostConnection;

/**
 * Class for making a HTTP get request to the server and retrieving a generic
 * list parsed from JSON. To define the class of the object in list to be
 * retrieved put its class name in the <> brackets and pass its class as the
 * first argument to the constructor. Use this by overriding the
 * handleResponseList() method and calling retrieveList() method.
 * 
 * @author Jaan Janno
 */

abstract public class GenericListPostLoader<T> extends GenericListLoader<T>
		implements Runnable {

	public GenericListPostLoader(Type typeToken, String url) {
		super(typeToken, url);
	}

	public GenericListPostLoader(Type typeToken, String url, String cookie) {
		super(typeToken, url, cookie);
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
	 * Override this method to define the behavior after a list has been
	 * retrieved. Remember this method doesn't run on the UI thread!
	 */

	abstract public void handleResponseList(List<T> responseList);

	abstract public void writeRequestBody(DataOutputStream writer)
			throws IOException;
}