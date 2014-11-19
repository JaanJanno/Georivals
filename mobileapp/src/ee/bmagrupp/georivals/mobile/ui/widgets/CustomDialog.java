package ee.bmagrupp.georivals.mobile.ui.widgets;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CustomDialog extends Dialog {
	private TextView messageTextView;
	private EditText inputEditText;
	private Button positiveButton;
	private Button negativeButton;
	private final CustomDialog customDialog = this;

	public CustomDialog(Activity activity) {
		super(activity);
		setup();
	}

	private void setup() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_layout);

		messageTextView = (TextView) findViewById(R.id.dialog_message);
		inputEditText = (EditText) findViewById(R.id.dialog_input);
		positiveButton = (Button) findViewById(R.id.dialog_positive_button);
		negativeButton = (Button) findViewById(R.id.dialog_negative_button);

		messageTextView.setTypeface(MainActivity.GABRIOLA_FONT);
		inputEditText.setTypeface(MainActivity.GABRIOLA_FONT);
		positiveButton.setTypeface(MainActivity.GABRIOLA_FONT);
		negativeButton.setTypeface(MainActivity.GABRIOLA_FONT);

		negativeButton
				.setOnClickListener(new android.view.View.OnClickListener() {
					@Override
					public void onClick(View v) {
						customDialog.dismiss();
					}
				});
	}

	public CustomDialog setMessage(String message) {
		messageTextView.setText(message);
		return this;
	}

	public CustomDialog setInput(String hint) {
		inputEditText.setHint(hint);
		return this;
	}

	public String getInputValue() {
		return inputEditText.getText().toString();
	}

	public CustomDialog hideInput() {
		inputEditText.setVisibility(View.GONE);
		return this;
	}

	public CustomDialog setPositiveButton(String positiveButtonName,
			android.view.View.OnClickListener positiveButtonClickListener) {
		positiveButton.setText(positiveButtonName);
		positiveButton.setOnClickListener(positiveButtonClickListener);
		return this;
	}

	public CustomDialog setPositiveButton(
			android.view.View.OnClickListener positiveButtonClickListener) {
		positiveButton.setOnClickListener(positiveButtonClickListener);
		return this;
	}

	public CustomDialog setPositiveButton(String positiveButtonName) {
		positiveButton.setText(positiveButtonName);
		return this;
	}

	public CustomDialog setNegativeButton(String negativeButtonName,
			android.view.View.OnClickListener negativeButtonClickListener) {
		negativeButton.setText(negativeButtonName);
		negativeButton.setOnClickListener(negativeButtonClickListener);
		return this;
	}

	public CustomDialog setNegativeButton(
			android.view.View.OnClickListener negativeButtonClickListener) {
		negativeButton.setOnClickListener(negativeButtonClickListener);
		return this;
	}

	public CustomDialog setNegativeButton(String negativeButtonName) {
		negativeButton.setText(negativeButtonName);
		return this;
	}

}
