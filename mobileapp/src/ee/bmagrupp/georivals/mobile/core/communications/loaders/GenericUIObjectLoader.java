package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.util.List;
import android.app.Activity;
import ee.bmagrupp.georivals.mobile.core.communications.Connection;

/**
 * Class for making a HTTP get request to the server and retrieving a
 * generic object parsed from JSON and handling it on UI.
 * To define the class of the object to be retrieved put its class name in the <>
 * brackets and pass its class as the first argument to the constructor.
 * Use this by overriding the handleResponseList() method and calling 
 * retrieveObject() method.
 * @author	Jaan Janno
 */

abstract public class GenericUIObjectLoader<T> extends GenericObjectLoader<T> {

	private final Activity activity;
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param activity The activity that executes handleResponseObjectOnUI
	 * @param url
	 * @param cookie
	 */
	
	public GenericUIObjectLoader(Class<T> typeParameterClass, Activity activity, String url, String cookie) {
		super(typeParameterClass, url, cookie);
		this.activity = activity;
	}
	
	/**
	 * 
	 * @param typeParameterClass Class of the object to be received.
	 * @param activity The activity that executes handleResponseObjectOnUI
	 * @param url
	 * @param cookie
	 */

	public GenericUIObjectLoader(Class<T> typeParameterClass, Activity activity, String url) {
		super(typeParameterClass, url);
		this.activity = activity;
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
				handleUI(object);	
			}

			private void handleUI(final T object) {
				activity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						handleResponseObjectOnUI(object);
					}
				});
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
	 * This runs on the UI thread.
	 */
	
	abstract public void handleResponseObjectOnUI(T responseObject);
}
