package com.yborisjuk.vendor.fragments;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.activity.MainActivity;
import com.yborisjuk.vendor.libs.mySharedPreferences;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ContactFragment extends Fragment {

	private EditText inputName, inputEmail, inputMessage;
	private Button btn_send;
	private mySharedPreferences gvs;
	private String name, email, messsage;
	private mySharedPreferences sharedPreferences;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_screen_frg, container,
				false);
		return view;
	};

	@Override
	public void onStart() {
		super.onStart();

		setHasOptionsMenu(true);
		gvs = new mySharedPreferences(getActivity());
		inputName = (EditText) getActivity().findViewById(
				R.id.contact_scr_edtxt_name);
		inputEmail = (EditText) getActivity().findViewById(
				R.id.contact_scr_edtxt_email);
		inputMessage = (EditText) getActivity().findViewById(
				R.id.contact_scr_edtxt_message);

		inputName.setText(gvs.getName());
		inputEmail.setText(gvs.getEmail());

		btn_send = (Button) getActivity().findViewById(
				R.id.contact_scr_btn_send);
		btn_send.setOnClickListener(clickListener);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			name = inputName.getText().toString();
			email = inputEmail.getText().toString();
			messsage = inputMessage.getText().toString();

			Intent sendEmail = new Intent(Intent.ACTION_SEND);
			sendEmail.setData(Uri.parse("mailto:"));
			sendEmail.setType("text/plain");

			sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			sendEmail.putExtra(Intent.EXTRA_SUBJECT, name);
			sendEmail.putExtra(Intent.EXTRA_TEXT, messsage);

			try {
				startActivity(Intent.createChooser(sendEmail, "Send email..."));
				getActivity().finish();
				Log.i("Finished sending email...", "");
			} catch (android.content.ActivityNotFoundException e) {
				Toast.makeText(getActivity(),
						"There is no email client installed.",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.additional_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.logout) {
			sharedPreferences = new mySharedPreferences(getActivity());
			sharedPreferences.clearPrefence();

			Intent loginIntent = new Intent(getActivity(),
					LoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(loginIntent);
			getActivity().finish();
		} else if (id == R.id.back) {
			Intent homeIntent = new Intent(getActivity(),
					MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			getActivity().finish();
		}
		return super.onOptionsItemSelected(item);

	}
}
