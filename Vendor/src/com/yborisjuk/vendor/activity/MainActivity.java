package com.yborisjuk.vendor.activity;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.fragments.ContactFragment;
import com.yborisjuk.vendor.fragments.FavoriteFragment;
import com.yborisjuk.vendor.fragments.HomeFragment;
import com.yborisjuk.vendor.fragments.SettingFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

public class MainActivity extends FragmentActivity {
	public FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);

		
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setBackgroundColor(Color.parseColor("#66C487"));

		Bundle bundle = new Bundle();
		mTabHost.addTab(mTabHost.newTabSpec("home").setIndicator("Home"),
				HomeFragment.class, bundle);

		bundle = new Bundle();
		mTabHost.addTab(mTabHost.newTabSpec("favorite")
				.setIndicator("Favorite"), FavoriteFragment.class, bundle);

		bundle = new Bundle();
		mTabHost.addTab(mTabHost.newTabSpec("setting").setIndicator("Setting"),
				SettingFragment.class, bundle);

		bundle = new Bundle();
		mTabHost.addTab(mTabHost.newTabSpec("contact").setIndicator("Contact"),
				ContactFragment.class, bundle);

	}
}
