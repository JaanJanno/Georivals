package ee.bmagrupp.georivals.mobile.ui.widgets;

import ee.bmagrupp.georivals.mobile.R;
import ee.bmagrupp.georivals.mobile.ui.MainActivity;
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

	public CustomDialog(MainActivity activity) {
		super(activity);
		initialize();
	}

	/**
	 * Sets the initial attributes of the dialog.
	 */

	private void initialize() {
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

	/**
	 * Sets the message of the dialog.
	 * 
	 * @param message
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setMessage(String message) {
		messageTextView.setText(message);
		return this;
	}

	/**
	 * Sets the hint of the dialog input view.
	 * 
	 * @param hint
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setInputHint(String hint) {
		inputEditText.setHint(hint);
		return this;
	}

	/**
	 * @return The value of the dialog input.
	 */

	public String getInputValue() {
		return inputEditText.getText().toString();
	}

	/**
	 * Hides the dialog input view.
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog hideInput() {
		inputEditText.setVisibility(View.GONE);
		return this;
	}

	/**
	 * Sets the positive button of the dialog.
	 * 
	 * @param buttonText
	 * @param buttonClickListener
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setPositiveButton(String buttonText,
			android.view.View.OnClickListener buttonClickListener) {
		positiveButton.setText(buttonText);
		positiveButton.setOnClickListener(buttonClickListener);
		return this;
	}

	/**
	 * Sets the positive button of the dialog.
	 * 
	 * @param buttonClickListener
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setPositiveButton(
			android.view.View.OnClickListener buttonClickListener) {
		positiveButton.setOnClickListener(buttonClickListener);
		return this;
	}

	/**
	 * Sets the positive button of the dialog.
	 * 
	 * @param buttonText
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setPositiveButton(String buttonText) {
		positiveButton.setText(buttonText);
		return this;
	}

	/**
	 * Sets the negative button of the dialog.
	 * 
	 * @param buttonText
	 * @param buttonClickListener
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setNegativeButton(String buttonText,
			android.view.View.OnClickListener buttonClickListener) {
		negativeButton.setText(buttonText);
		negativeButton.setOnClickListener(buttonClickListener);
		return this;
	}

	/**
	 * Sets the negative button of the dialog.
	 * 
	 * @param buttonClickListener
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setNegativeButton(
			android.view.View.OnClickListener buttonClickListener) {
		negativeButton.setOnClickListener(buttonClickListener);
		return this;
	}

	/**
	 * Sets the negative button of the dialog.
	 * 
	 * @param buttonText
	 * 
	 * @return The updated dialog.
	 */

	public CustomDialog setNegativeButton(String buttonText) {
		negativeButton.setText(buttonText);
		return this;
	}

}
