package com.yborisjuk.vendor.libs.json;

import java.io.IOException;
import java.net.URL;

import com.yborisjuk.vendor.activity.DetailActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class LoadUrlForImage extends AsyncTask<String, String, Bitmap>{
	
	//static AsyncTask<Void,Integer,Integer> myAsyncTaskInstance = null;
	private Context context;
	private ProgressDialog pDialog;
	
	public LoadUrlForImage(Context context) {
		
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
	protected Bitmap doInBackground(String... params) {
		Bitmap bitmap = null;
		try {
			URL url = new URL(((DetailActivity) context).getIntent().getStringExtra("imgLink"));
			bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	@Override
	protected void onPostExecute(Bitmap result) {
		super.onPostExecute(result);
		pDialog.dismiss();
		((DetailActivity) context).imgLogo.setImageBitmap(result);
	}
}
