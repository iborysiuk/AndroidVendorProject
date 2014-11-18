package com.yborisjuk.vendor.libs.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.activity.RegisterActivity;
import com.yborisjuk.vendor.libs.UserFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class CreateUser extends AsyncTask<String, String, String> {

	private JSONParser jsonParser = new JSONParser();
	private UserFunction usrFunction = new UserFunction();
	private ProgressDialog pDialog;
	private Context context;
	private Intent logIntent;

	public CreateUser(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		pDialog = new ProgressDialog(context);
		pDialog.setMessage("Please wait...");
		pDialog.setIndeterminate(false);
		pDialog.setCancelable(true);
		pDialog.show();
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			JSONObject jsonObject = jsonParser.makeHTTPRequest(
					usrFunction.URL(), usrFunction.registerUser());
			int success = jsonObject.getInt("success");
			if (success == 1) {
				logIntent = new Intent(context, LoginActivity.class);
				logIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(logIntent);
				((RegisterActivity) context).finish();
			} else {
				Log.e("error_msg",
						"error_msg: " + jsonObject.getString("error_msg"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		pDialog.dismiss();
	}
}
