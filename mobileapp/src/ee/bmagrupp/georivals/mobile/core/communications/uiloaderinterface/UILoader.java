package ee.bmagrupp.georivals.mobile.core.communications.uiloaderinterface;

import android.app.Activity;

public class UILoader {

	public static <T, L extends UILoadable<T>> void load(final T object,
			final L loader, Activity activity) {
		activity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				loader.handleResponseInUI(object);
			}
		});
		loader.handleResponseInBackground(object);
	}

}