package ee.bmagrupp.aardejaht.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import ee.bmagrupp.aardejaht.R;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_layout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void register(View v) {
		EditText usernameEditText = (EditText) findViewById(R.id.register_username);
		EditText emailEditText = (EditText) findViewById(R.id.register_email);
		String username = usernameEditText.getText().toString();
		String email = emailEditText.getText().toString();
		if (username.equals("")) {
			MainActivity.showMessage(this, "Username must be filled!");
		} else {
			registrationRequest(username, email);
		}
	}

	private void registrationRequest(String username, String email) {

	}
}