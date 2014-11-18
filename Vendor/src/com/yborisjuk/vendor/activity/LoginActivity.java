package com.yborisjuk.vendor.activity;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.libs.CheckNetworkConnection;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.UserFunction;
import com.yborisjuk.vendor.libs.mySharedPreferences;
import com.yborisjuk.vendor.libs.json.AttemptLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private Button btn_login;
	public EditText inputEmail, inputPassword;
	private TextView registerNewUser;
	private String email, password;
	private UserFunction usrfunc;
	private mySharedPreferences mySharedPreferences;
	private  CheckNetworkConnection connection;
	/**
	 * List of errors
	 */
	// If the form is empty (email & password)
	private static final String err_1 = "Please enter your email and password";

	// Invalid email
	private static final String err_2 = "Please enter your email";

	// Invalid password
	private static final String err_3 = "Please enter your password";

	// Incorrect email
	private static final String err_4 = "Invalid Email Addresss";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mySharedPreferences = new mySharedPreferences(getApplicationContext());
		connection = new CheckNetworkConnection();

		if (mySharedPreferences.checkPrefence() && connection.isConnected(getApplicationContext())) {
			Log.i("Connection", "Connect: successful");
			Intent intent = new Intent(getApplicationContext(),
					MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
		/*	Toast.makeText(
					getApplicationContext(),
					"Unexpected Error occcured! [Most common Error: Device might "
							+ "not be connected to Internet]",
					Toast.LENGTH_LONG).show();*/

			setContentView(R.layout.login_screen);
			btn_login = (Button) findViewById(R.id.login_scr_btn_login);
			inputEmail = (EditText) findViewById(R.id.login_scr_edtxt_email);
			inputPassword = (EditText) findViewById(R.id.login_scr_edtxt_password);
			registerNewUser = (TextView) findViewById(R.id.login_scr_txt_register);

			inputEmail.setText(mySharedPreferences.getEmail());
			inputPassword.setText(mySharedPreferences.getPassword());

			btn_login.setOnClickListener(clickListener);
			registerNewUser.setOnClickListener(clickListener);
		}
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int i = v.getId();
			if (i == R.id.login_scr_btn_login) {
				// Get values from text fields
				if (connection.isConnected(getApplicationContext())) {
					email = inputEmail.getText().toString();
					password = inputPassword.getText().toString();
					usrfunc = new UserFunction();
					if ((email.isEmpty()) && (password.isEmpty())) {

						Toast.makeText(getApplicationContext(), err_1,
								Toast.LENGTH_LONG).show();

					} else if (email.isEmpty()) {

						Toast.makeText(getApplicationContext(), err_2,
								Toast.LENGTH_LONG).show();

					} else if (password.isEmpty()) {

						Toast.makeText(getApplicationContext(), err_3,
								Toast.LENGTH_LONG).show();

					} else if (!usrfunc.cheakEmail(email)) {

						Toast.makeText(getApplicationContext(), err_4,
								Toast.LENGTH_LONG).show();
						clearField();

					} else {
						new GlobalVariableSetting(email, password);
						new AttemptLogin(LoginActivity.this).execute();
					}
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Unexpected Error occcured! [Most common Error: Device might "
									+ "not be connected to Internet]",
							Toast.LENGTH_LONG).show();
				}

			}else if (i == R.id.login_scr_txt_register) {
				// Intent a Register activity that allows to create a new user
				if (connection.isConnected(getApplicationContext())) {
					Intent intent = new Intent(getApplicationContext(),
							RegisterActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				} else {
					Toast.makeText(
							getApplicationContext(),
							"Unexpected Error occcured! [Most common Error: Device might "
									+ "not be connected to Internet]",
							Toast.LENGTH_LONG).show();
				}
			}

		}
	};


	// Clear all of fields(Email and Password)
	private void clearField() {
		inputEmail.setText("");
		inputPassword.setText("");
	}
}
