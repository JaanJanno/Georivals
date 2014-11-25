package ee.bmagrupp.georivals.mobile.core.communications.loaders;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import ee.bmagrupp.georivals.mobile.core.communications.Connection;
import ee.bmagrupp.georivals.mobile.core.communications.GsonParser;
import ee.bmagrupp.georivals.mobile.core.communications.PostConnection;

/**
 * Class for making a HTTP post request to the server and retrieving a generic
 * response parsed from JSON. To define the type/class of the response to be
 * retrieved put its type/class name in the <> brackets and pass its class as
 * the first argument to the constructor. Use this by overriding the
 * handleResponse() method and calling retrieveResponse() method.
 * 
 * @author Jaan Janno
 */

public abstract class GenericPostLoader<T> extends GenericLoader<T> {

	Object post; // Object sent to the server.

	/**
	 * 
	 * @param typeToken
	 *            Type of the response.
	 * @param post
	 *            The object to be posted to the server.
	 * @param url
	 *            Url of the request.
	 */

	public GenericPostLoader(Type typeToken, Object post, String url) {
		super(typeToken, url, "");
		this.post = post;
	}

	/**
	 * 
	 * @param typeToken
	 *            Type of the response.
	 * @param post
	 *            The object to be posted to the server.
	 * @param url
	 *            Url of the request.
	 * @param cookie
	 *            Cookie string sent along with the request.
	 */

	public GenericPostLoader(Type typeToken, Object post, String url,
			String cookie) {
		super(typeToken, url, cookie);
		this.post = post;
	}

	/**
	 * 
	 * @param typeParameterClass
	 * @param url
	 */

	public GenericPostLoader(Class<T> typeClass, Object post,
			String url) {
		super(typeClass, url, "");
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
	 *            Cookie string sent along with the request.
	 */

	public GenericPostLoader(Class<T> typeClass, Object post,
			String url, String cookie) {
		super(typeClass, url, cookie);
		this.post = post;
	}

	/*
	 * Creates a new connection that supports sending an object to server.
	 */

	@Override
	protected Connection createConnection() {
		return new PostConnection(url, cookie) {

			@Override
			public void handleResponseBody(String response) {
				T object = getObjectFromJSON(response);
				handleResponse(object);
			}

			@Override
			public void handleResponseCookies(List<String> cookies) {
				// No cookies expected.
			}

			@Override
			public void writeToConnection(DataOutputStream writer)
					throws IOException {
				String json = GsonParser.getInstance().toJson(post);
				writer.writeBytes(json);
			}
		};
	}

}
