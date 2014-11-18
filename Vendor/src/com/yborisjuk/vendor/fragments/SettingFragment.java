package com.yborisjuk.vendor.fragments;

import java.io.ByteArrayOutputStream;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.activity.MainActivity;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.mySharedPreferences;
import com.yborisjuk.vendor.libs.json.UpdateUser;

public class SettingFragment extends Fragment {

	private EditText getName, getEmail, getPassword, getCity;
	private Button btn_update;
	private ImageView imgCamera;
	private TextView setName;
	private mySharedPreferences mySharedPreferences;
	private GlobalVariableSetting gvs = new GlobalVariableSetting();
	private mySharedPreferences sharedPreferences;
	private Uri picUri;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.setting_screen_frg, container,
				false);
		return view;
	};

	@Override
	public void onStart() {
		super.onStart();

		setHasOptionsMenu(true);

		getName = (EditText) getActivity().findViewById(
				R.id.setting_scr_edtxt_name);
		getEmail = (EditText) getActivity().findViewById(
				R.id.setting_scr_edtxt_email);
		getPassword = (EditText) getActivity().findViewById(
				R.id.setting_scr_edtxt_password);
		getCity = (EditText) getActivity().findViewById(
				R.id.setting_scr_edtxt_city);
		imgCamera = (ImageView) getActivity().findViewById(
				R.id.setting_scr_camera);
		setName = (TextView) getActivity().findViewById(
				R.id.setting_txt_usrname);

		mySharedPreferences = new mySharedPreferences(getActivity());
		getName.setText(mySharedPreferences.getName());
		getEmail.setText(mySharedPreferences.getEmail());
		gvs.setUID(mySharedPreferences.getUID());
		getCity.setText(mySharedPreferences.getCity());
		setName.setText(mySharedPreferences.getName());
		if (gvs.getBitmap() != null) {
			imgCamera.setImageBitmap(gvs.getBitmap());
		} else {
			imgCamera.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
		}
		

		btn_update = (Button) getActivity().findViewById(
				R.id.setting_scr_btn_update);

		btn_update.setOnClickListener(clickListener);
		imgCamera.setOnClickListener(clickListener);
	}
	
	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn_update) {
				String password = getPassword.getText().toString();
				gvs.setPassword(password);
				String city = getCity.getText().toString();
				gvs.setCity(city);

				new UpdateUser(getActivity()).execute();
			} else if (v == imgCamera) {
				try {
					Intent capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(capture, 1);
				} catch (ActivityNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == getActivity().RESULT_OK) {
			if (requestCode == 1) {
				picUri = data.getData();
				performCrop();
			} else if (requestCode == 2) {
				Bundle bundle = data.getExtras();
				Bitmap pic = bundle.getParcelable("data");
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); 
				pic.compress(Bitmap.CompressFormat.PNG, 90, outputStream); 
				byte[] bs = outputStream.toByteArray(); 
				String image = Base64.encodeToString(bs, Base64.DEFAULT); 
				sharedPreferences = new mySharedPreferences(getActivity());
				gvs.setUID(sharedPreferences.getUID()); 
				gvs.setImage(image);
				gvs.setBitmap(pic);
				imgCamera.setImageBitmap(pic);
			}
		}
	}

	private void performCrop() {
		try {
			Intent crop = new Intent("com.android.camera.action.CROP");
			crop.setDataAndType(picUri, "image/*");
			crop.putExtra("crop", "true");
			crop.putExtra("aspectX", 1);
			crop.putExtra("aspectY", 1);
			crop.putExtra("outputX", 256);
			crop.putExtra("outputY", 256);
			crop.putExtra("return-data", true);
			startActivityForResult(crop, 2);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.additional_menu, menu);
	};

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
		} else if (id == R.id.back) {
			Intent homeIntent = new Intent(getActivity(), MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			getActivity().finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
