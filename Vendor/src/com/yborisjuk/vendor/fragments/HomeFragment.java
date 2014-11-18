package com.yborisjuk.vendor.fragments;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;
import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.lib.adapters.ViewPagerAdapter;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.UserFunction;
import com.yborisjuk.vendor.libs.mySharedPreferences;
import com.yborisjuk.vendor.libs.json.JSONParser;
import com.yborisjuk.vendor.libs.json.ResponseVendors;

public class HomeFragment extends Fragment {

	private mySharedPreferences sharedPreferences;
	private Button btn_getVendorsList;
	private ImageView imgCamera;
	private TextView userName;
	private ViewPager mPager;
	private ViewPagerAdapter mAdapter;
	private PageIndicator mIndicator;

	private List<GlobalVariableSetting> displayListVendors = new ArrayList<GlobalVariableSetting>();
	private String[] array_ImgLinks;
	private GlobalVariableSetting listVendors;

	private GlobalVariableSetting gvs = new GlobalVariableSetting();
	private Runnable animateViewPager;
	private Handler handler;
	private boolean stopSliding = false;
	private static final long ANIM_VIEWPAGER_DELAY = 2500;
	private ResponseImages responseImages;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater
				.inflate(R.layout.home_screen_frg, container, false);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		setHasOptionsMenu(true);
		btn_getVendorsList = (Button) getActivity().findViewById(
				R.id.home_scr_frg_btn_vendors);
		mPager = (ViewPager) getActivity().findViewById(R.id.pager);
		imgCamera = (ImageView) getActivity().findViewById(R.id.detail_scr_img);
		userName = (TextView) getActivity().findViewById(
				R.id.detail_scr_txt_user_name);
		sharedPreferences = new mySharedPreferences(getActivity());
		userName.setText(sharedPreferences.getName().toString());

		mIndicator = (CirclePageIndicator) getActivity().findViewById(
				R.id.indicator);

		btn_getVendorsList.setOnClickListener(clickListener);

	}

	public void runnable(final int size) {
		handler = new Handler();
		animateViewPager = new Runnable() {
			public void run() {
				if (!stopSliding) {
					if (mPager.getCurrentItem() == size - 1) {
						mPager.setCurrentItem(0);
					} else {
						mPager.setCurrentItem(mPager.getCurrentItem() + 1, true);
					}
					handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
				}
			}
		};
	}

	@Override
	public void onResume() {
		super.onResume();
		if (displayListVendors.size() == 0) {
			new ResponseImages(getActivity()).execute();
			new DownloadUserImage().execute();
		} else {
			mPager.setAdapter(new ViewPagerAdapter(getActivity()
					.getSupportFragmentManager(), displayListVendors,
					array_ImgLinks));
			mIndicator.setViewPager(mPager);
			runnable(displayListVendors.size());
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
			if (gvs.getBitmap() != null) {
				imgCamera.setImageBitmap(gvs.getBitmap());
			} else {
				imgCamera.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
			}
		}
	}

	@Override
	public void onPause() {
		if (responseImages != null) {
			responseImages.cancel(true);
		}

		if (handler != null) {
			handler.removeCallbacks(animateViewPager);
		}
		super.onPause();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.main_menu, menu);
		return;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.logout) {
			sharedPreferences = new mySharedPreferences(getActivity());
			sharedPreferences.clearPrefence();

			Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(loginIntent);
			getActivity().finish();
		}
		return super.onOptionsItemSelected(item);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn_getVendorsList) {
				new ResponseVendors(getActivity()).execute();
			}
		}
	};

	private class DownloadUserImage extends AsyncTask<String, String, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap = null;
			try {
				URL url = new URL(sharedPreferences.getImgLink());
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
			imgCamera.setImageBitmap(result);
			gvs.setBitmap(result);
		}
	}

	private class ResponseImages extends AsyncTask<String, String, String> {

		private JSONParser jsonParser = new JSONParser();
		private UserFunction usrFunction = new UserFunction();
		private Context context;
		private ProgressDialog pDialog;
		private int success;

		public ResponseImages(Context context) {
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
						usrFunction.URL(), usrFunction.getImageForSliders());
				success = jsonObject.getInt("success");
				if (success == 1) {
					JSONArray jsonArray = jsonObject.getJSONArray("slider");

					String[] array_sImgLink = new String[jsonArray.length()];

					int s = 0;

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject object = (JSONObject) jsonArray.get(i);
						array_sImgLink[s++] = object.getString("imgLink");
					}
					new GlobalVariableSetting(array_sImgLink);

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
			super.onPostExecute(result);
			pDialog.dismiss();
			array_ImgLinks = gvs.getArray_sImgLink();
			for (int i = 0; i < array_ImgLinks.length; i++) {
				listVendors = new GlobalVariableSetting(array_ImgLinks[i]);
				displayListVendors.add(listVendors);
			}
			mAdapter = new ViewPagerAdapter(getActivity()
					.getSupportFragmentManager(), displayListVendors,
					array_ImgLinks);

			mPager.setAdapter(mAdapter);
			mIndicator.setViewPager(mPager);
			runnable(displayListVendors.size());
			handler.postDelayed(animateViewPager, ANIM_VIEWPAGER_DELAY);
		}
	}
}
