package com.yborisjuk.vendor.lib.adapters;

import java.io.IOException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ViewFragment extends Fragment {

	public LinearLayout layout;
	public ImageView imageView;
	public Context context;

	private static final String KEY_CONTENT = "ViewFragment:Content";
	private String mContent = "???";

	public static ViewFragment newInstance(String content) {
		ViewFragment viewFragment = new ViewFragment();
		StringBuilder builder = new StringBuilder();
		builder.append(content).append(" ");
		viewFragment.mContent = builder.toString();

		return viewFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if ((savedInstanceState != null)
				&& savedInstanceState.containsKey(KEY_CONTENT)) {
			mContent = savedInstanceState.getString(KEY_CONTENT);
		}
	}
	@SuppressWarnings("deprecation")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		new LoadImageForSlider().execute();

		layout = new LinearLayout(getActivity());
		layout.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		layout.setGravity(Gravity.CENTER);
		layout.addView(imageView);
		return layout;
	}
	
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(KEY_CONTENT, mContent);
	}

	private class LoadImageForSlider extends AsyncTask<String, String, Bitmap> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			imageView = new ImageView(getActivity());
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				URL url = new URL(mContent);
				bitmap = BitmapFactory.decodeStream(url.openConnection()
						.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			imageView.setImageBitmap(result);
		}
	}
}
