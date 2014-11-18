package com.yborisjuk.vendor.libs.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yborisjuk.vendor.activity.MainActivity;
import com.yborisjuk.vendor.activity.VendorListActivity;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.UserFunction;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class ResponseVendors extends AsyncTask<String, String, String> {

	private JSONParser jsonParser = new JSONParser();
	private UserFunction usrFunction = new UserFunction();
	private Context context;
	private int success;
	private ProgressDialog pDialog;

	public ResponseVendors(Context context) {
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
					usrFunction.URL(), usrFunction.getVendors());
			success = jsonObject.getInt("success");
			if (success == 1) {
				JSONArray jsonArray = jsonObject.getJSONArray("vendor");

				String[] array_vUID = new String[jsonArray.length()];
				String[] array_vName = new String[jsonArray.length()];
				String[] array_vEmail = new String[jsonArray.length()];
				String[] array_vPhone = new String[jsonArray.length()];
				String[] array_vCountry = new String[jsonArray.length()];
				String[] array_vCity = new String[jsonArray.length()];
				String[] array_vAddress = new String[jsonArray.length()];
				String[] array_vPostalCode = new String[jsonArray.length()];
				String[] array_vImgLink = new String[jsonArray.length()];
				String[] array_vWorkTime = new String[jsonArray.length()];

				int v1 = 0, v2 = 0, v3 = 0, v4 = 0, v5 = 0, v6 = 0, v7 = 0, v8 = 0, v9 = 0, v10 = 0;

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = (JSONObject) jsonArray.get(i);
					array_vUID[v1++] = object.getString("unique_id");
					array_vName[v2++] = object.getString("name");
					array_vEmail[v3++] = object.getString("email");
					array_vPhone[v4++] = object.getString("phonenumber");
					array_vCountry[v5++] = object.getString("country");
					array_vCity[v6++] = object.getString("city");
					array_vAddress[v7++] = object.getString("address");
					array_vPostalCode[v8++] = object.getString("postalcode");
					array_vImgLink[v9++] = object.getString("imgLink");
					array_vWorkTime[v10++] = object.getString("workTime");
				}
				new GlobalVariableSetting(array_vUID ,array_vName, array_vEmail,
						array_vPhone, array_vCountry, array_vCity,
						array_vAddress, array_vPostalCode, array_vImgLink, array_vWorkTime);
				

				Log.e("error_msg", "congrats");
			} else {
				Log.e("error_msg", "error");
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
			Intent vInt = new Intent(context, VendorListActivity.class);
			vInt.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			context.startActivity(vInt);
			((MainActivity) context).finish();
		}
	}

}
