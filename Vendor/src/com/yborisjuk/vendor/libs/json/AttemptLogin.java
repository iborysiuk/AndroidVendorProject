package com.yborisjuk.vendor.libs.json;

import org.json.JSONException;
import org.json.JSONObject;

import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.activity.MainActivity;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.UserFunction;
import com.yborisjuk.vendor.libs.mySharedPreferences;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class AttemptLogin extends AsyncTask<String, String, String> {
	private JSONParser jsonParser = new JSONParser();
	private UserFunction usrFunction = new UserFunction();
	private GlobalVariableSetting gvs = new GlobalVariableSetting();
	private ProgressDialog pDialog;
	private Context context;
	private Intent mIntent;
	private int success;
	private mySharedPreferences mySharedPreferences;
	
	public AttemptLogin(Context context) {
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
					usrFunction.URL(), usrFunction.loginUser());
			success = jsonObject.getInt("success");
			if (success == 1) {
				Log.i("success_msg", "success_msg: Congratulations!!");
				gvs.setUID(jsonObject.getString("uid"));
				gvs.setName(jsonObject.getJSONObject("user").getString("name"));
				gvs.setCity(jsonObject.getJSONObject("user").getString("city"));
				gvs.setImage(jsonObject.getJSONObject("user").getString("imgLink"));
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
		if (success == 1) {
			
			mySharedPreferences = new mySharedPreferences(context);
			mySharedPreferences.createPreference();
			
			mIntent = new Intent(context, MainActivity.class);
			mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(mIntent);
			((LoginActivity) context).finish();
			
		} else {
			
			Toast.makeText(((LoginActivity) context),
					"Incorrect email or password!", Toast.LENGTH_LONG).show();
			((LoginActivity) context).inputEmail.setText("");
			((LoginActivity) context).inputPassword.setText("");
		
		}
	}

}
