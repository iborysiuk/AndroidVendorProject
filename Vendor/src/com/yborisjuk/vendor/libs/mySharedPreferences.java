package com.yborisjuk.vendor.libs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class mySharedPreferences {

	private SharedPreferences preferences;
	private Context context;
	private GlobalVariableSetting gvs;

	private static final String LOGIN_PREFERENCES = "logPrefs";
	private static final String logEmail = "emailKey";
	private static final String logPass = "passKey";
	private static final String logUID = "uidKey";
	private static final String logName = "nameKey";
	private static final String logCity = "cityKey";
	private static final String logImgLink = "imgLinkKey";

	public mySharedPreferences(Context context) {
		this.context = context;
	}

	public void createPreference() {
		gvs = new GlobalVariableSetting();
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(logUID, gvs.getUID());
		editor.putString(logName, gvs.getName());
		editor.putString(logEmail, gvs.getEmail());
		editor.putString(logPass, gvs.getPassword());
		editor.putString(logCity, gvs.getCity());
		editor.putString(logImgLink, gvs.getImage());
		editor.commit();
	}

	public boolean checkPrefence() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String emailPrefs = preferences.getString(logEmail, null);
		String passPrefs = preferences.getString(logPass, null);

		if (emailPrefs != null && passPrefs != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public void clearPrefence(){
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.clear().commit();
	}
	
	public String getUID() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logUID, null);
		return result;
	}
	
	public String getName() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logName, null);
		return result;
	}
	
	public String getEmail() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logEmail, null);
		return result;
	}
	
	public String getPassword() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logPass, null);
		return result;
	}
	public String getCity() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logCity, null);
		return result;
	}
	
	public String getImgLink() {
		preferences = context.getSharedPreferences(LOGIN_PREFERENCES,
				Context.MODE_PRIVATE);
		String result = preferences.getString(logImgLink, null);
		return result;
	}
	
}
