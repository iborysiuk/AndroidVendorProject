package com.yborisjuk.vendor.libs;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class UserFunction {

	private GlobalVariableSetting gvs = new GlobalVariableSetting();
	private final String URL = "http://androidvendors.t15.org/main.php";

	public String URL() {
		return this.URL;
	}

	public List<NameValuePair> loginUser() {
		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("tag", "login"));
		values.add(new BasicNameValuePair("email", gvs.getEmail()));
		values.add(new BasicNameValuePair("password", gvs.getPassword()));
		return values;

	}

	public List<NameValuePair> registerUser() {

		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("tag", "register"));
		values.add(new BasicNameValuePair("name", gvs.getName()));
		values.add(new BasicNameValuePair("email", gvs.getEmail()));
		values.add(new BasicNameValuePair("password", gvs.getPassword()));
		values.add(new BasicNameValuePair("city", gvs.getCity()));
		values.add(new BasicNameValuePair("image", gvs.getImage()));
		return values;
	}
	
	public List<NameValuePair> updateUser() {

		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("tag", "update"));
		values.add(new BasicNameValuePair("uid", gvs.getUID()));
		values.add(new BasicNameValuePair("password", gvs.getPassword()));
		values.add(new BasicNameValuePair("city", gvs.getCity()));
		values.add(new BasicNameValuePair("image", gvs.getImage()));
		return values;
	}

	public List<NameValuePair> getVendors() {

		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("tag", "vendor"));
		return values;
	}

	public List<NameValuePair> getImageForSliders() {

		List<NameValuePair> values = new ArrayList<NameValuePair>();
		values.add(new BasicNameValuePair("tag", "slider"));
		return values;
	}

	public boolean cheakEmail(String email) {
		return EMAIL_ADDRESS.matcher(email).matches();
	}

	public static final Pattern EMAIL_ADDRESS = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" + ")+");

}
