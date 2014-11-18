package com.yborisjuk.vendor.activity;

import java.io.ByteArrayOutputStream;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.mySharedPreferences;
import com.yborisjuk.vendor.libs.json.CreateUser;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class RegisterActivity extends Activity {

	private EditText inputName, inputEmail, inputPassword, inputCity;
	private ImageView imgCamera;
	private TextView setName;
	private Button btn_register;
	private String name, email, password, city;
	private Uri picUri;
	
	private mySharedPreferences sharedPreferences;
	private GlobalVariableSetting gvs = new GlobalVariableSetting();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_screen);

		inputName = (EditText) findViewById(R.id.register_scr_edtxt_usrname);
		inputEmail = (EditText) findViewById(R.id.register_scr_edtxt_email);
		inputPassword = (EditText) findViewById(R.id.register_scr_edtxt_password);
		inputCity = (EditText) findViewById(R.id.register_scr_edtxt_city);
		imgCamera = (ImageView) findViewById(R.id.register_scr_camera);
		setName = (TextView) findViewById(R.id.register_scr_txt_usrname);

		btn_register = (Button) findViewById(R.id.register_scr_btn_register);
		
		imgCamera.setOnClickListener(clickListener);
		btn_register.setOnClickListener(clickListener);

		inputName.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setName.setText(s);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btn_register) {
				name = inputName.getText().toString();
				email = inputEmail.getText().toString();
				password = inputPassword.getText().toString();
				city = inputCity.getText().toString();

				new GlobalVariableSetting(name, email, password, city);
				new CreateUser(RegisterActivity.this).execute();
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
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
				sharedPreferences = new mySharedPreferences(getApplication());
				gvs.setUID(sharedPreferences.getUID()); 
				gvs.setImage(image);
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
}
