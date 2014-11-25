package ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface;

public interface UILoadable<T> {

	/**
	 * Override to handle retrieved response in UI.
	 * 
	 * @param response
	 */

	public void handleResponseInUI(T response);

	/**
	 * Override to handle retrieved response in background.
	 * 
	 * @param response
	 */

	public void handleResponseInBackground(T response);

}
