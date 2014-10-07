package ee.bmagrupp.aardejaht.core.communications;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Thread.State;
import java.net.HttpURLConnection;
import java.net.URL;
import ee.bmagrupp.aardejaht.core.communications.exceptions.DoubleRequestException;
import android.os.StrictMode;
import android.util.Log;

/**
 * @author	Jaan Janno
 */

/**
 * Class for making a HTTP get request. Request runs on separate thread. Capable
 * of retrieving a response in string format. One Connection object can only generate
 * one thread to avoid multiple threads waiting to write to the same string. Thus
 * use one Connection per request!
 * 
 * Each object holds an immutable URL with which to communicate. Request is
 * made after calling sendRequest() method. Response to the request can be
 * retrieved by getResponse() method after the request has been finished.
 */
 
public class Connection implements Runnable {

	private Thread thread;
	private String urlString;	// URL of the connection destination.
	private String response;	// Response from the URL.

	public Connection(String urlString) {
		this.urlString = urlString;
		this.thread = new Thread(this);
	}
	
	/**
	 * Starts the thread which makes a request to the URL in urlString
	 * field. Response gets stored in "response" field.
	 * One Connection object can only run this once!
	 */
	
	public void sendRequest() {	
		if (thread.getState() == State.NEW)
			thread.start();
		else
			throw new DoubleRequestException("Request has already been run!");
	}
	
	/**
	 * Returns the response collected from calling the sendRequest() method.
	 * Returns null if called before request is finished!
	 */

	public String getResponse() throws NullPointerException {
		return response;
	}
	
	/**
	 * Waits for the response to arrive. Do not call from UI thread!
	 */

	public void join() throws InterruptedException {
		thread.join();
	}
	
	/**
	 * Method called by separate thread. Call sendRequest() instead of this!
	 */

	@Override
	public void run() {
		try {
			setPolicy();
			response = httpRequest(urlString);
			Log.v("Connection", "Retrieved response from: " + urlString);
		} catch (Exception e) {
			Log.v("Connection", "Failed to retrieve response from: " + urlString);
		}
	}

	/*
	 * Sets safer thread policy.
	 */
	
	private void setPolicy() {
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	/*
	 * Connects to given URL and returns its response string.
	 */

	private String httpRequest(String urlString) throws Exception {
		HttpURLConnection connection = getConnection(urlString);
		return readStream(connection.getInputStream());
	}
	
	/*
	 * Handles connecting to given URL.
	 */

	private HttpURLConnection getConnection(String urlString) throws Exception {
		URL url = new URL(urlString);
		return (HttpURLConnection) url.openConnection();
	}
	
	/*
	 * Reads input stream incoming from the server.
	 */
	
	private String readStream(InputStream in) throws Exception {
		String serverResponse = new String();
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		for (String line = reader.readLine(); line != null;) {
			serverResponse += '\n' + line;
			line = reader.readLine();
		}
		reader.close();
		return serverResponse;
	}
}
