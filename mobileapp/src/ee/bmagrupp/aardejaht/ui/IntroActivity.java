package ee.bmagrupp.aardejaht.ui;

import ee.bmagrupp.aardejaht.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IntroActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro_layout);
	}

	public void login(View v) {
		Intent loginIntent = new Intent(this, LoginActivity.class);
		startActivityForResult(loginIntent, MainActivity.LOGIN_REQUEST);
	}

	public void register(View v) {
		Intent registerIntent = new Intent(this, RegisterActivity.class);
		startActivityForResult(registerIntent,
				MainActivity.REGISTRATION_REQUEST);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK,
					getIntent().putExtra("requestCode", requestCode));
			finish();
		}

	}
}
