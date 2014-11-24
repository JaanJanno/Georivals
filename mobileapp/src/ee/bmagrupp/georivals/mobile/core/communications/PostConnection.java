package ee.bmagrupp.georivals.mobile.core.communications;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.List;

/**
 * Class for making a HTTP post request as default. Request method can be
 * changed using the setRequestMethod() method. Request runs on separate thread.
 * Capable of retrieving a response and a list of cookies in string format. Each
 * object holds an immutable URL with which to communicate. Request is made
 * after calling sendRequest() method. Response to the request can be handled in
 * the overridden handleResponseBody() method. Retrieved cookies can be handled
 * in the overridden handleResponseCookies() method. The body of the request can
 * be constructed using the overridden writeToConnection() method.
 * 
 * @author Jaan Janno
 */

public abstract class PostConnection extends Connection {

	/**
	 * 
	 * @param urlString
	 *            URL connected to.
	 */

	public PostConnection(String urlString) {
		super(urlString);
	}

	/**
	 * 
	 * @param urlString
	 *            URL connected to.
	 * @param cookie
	 *            Cookie string sent along the request.
	 */

	public PostConnection(String urlString, String cookie) {
		super(urlString, cookie);
	}

	/*
	 * Connects to given URL and returns its response string.
	 */

	protected void httpRequest(HttpURLConnection connection) throws Exception {
		handleRequestProperties(connection, true, getRequestMethod());
		doIo(connection);
		List<String> cookies = collectResponseCookies(connection);
		handleResponseCookies(cookies);
	}

	/*
	 * Collects a response from server and calls its handling method.
	 */

	private void doIo(HttpURLConnection connection) {
		String serverResponse = "";
		try {
			writeStream(connection.getOutputStream());
			serverResponse = readStream(connection.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		handleResponseBody(serverResponse);
	}

	/*
	 * Sends output stream to the server.
	 */

	private void writeStream(OutputStream out) throws Exception {
		DataOutputStream writer = new DataOutputStream(out);
		writeToConnection(writer);
		writer.flush();
		writer.close();
	}

	/**
	 * Override this to write a request body for the server. Closing the writer
	 * is not required as it is done automatically.
	 * 
	 * @param writer
	 * @throws IOException
	 */

	public abstract void writeToConnection(DataOutputStream writer)
			throws IOException;

}
