package com.yborisjuk.vendor.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.activity.DetailActivity;
import com.yborisjuk.vendor.activity.LoginActivity;
import com.yborisjuk.vendor.activity.MainActivity;
import com.yborisjuk.vendor.lib.adapters.VendorsAdapter;
import com.yborisjuk.vendor.libs.DBVendorHelper;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.mySharedPreferences;

public class FavoriteFragment extends Fragment {

	private DBVendorHelper db;
	private ListView favorite_list;
	private EditText searchView;
	private GlobalVariableSetting listVendors;
	private VendorsAdapter vAdapter;
	private List<GlobalVariableSetting> displayListVendors = new ArrayList<GlobalVariableSetting>();
	private List<GlobalVariableSetting> tempDispalyListVendors = new ArrayList<GlobalVariableSetting>();
	private String[] array_uids, array_Names, array_Emails, array_Phones,
			array_Countries, array_Cities, array_Addresses, array_PostalCodes,
			array_ImgLinks, array_WorkTimes;
	private String[] array_tempUids, array_tempNames, array_tempEmails,
			array_tempPhones, array_tempCountries, array_tempCities,
			array_tempAddresses, array_tempPostalCodes, array_tempImgLinks,
			array_tempWorkTimes;
	private Cursor cursor;
	private int textLength;
	private mySharedPreferences sharedPreferences;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favorite_screen_frg, container,
				false);
		return view;
	};

	@Override
	public void onStart() {
		super.onStart();
		
		setHasOptionsMenu(true);

		db = new DBVendorHelper(getActivity());
		favorite_list = (ListView) getActivity().findViewById(
				R.id.favorite_scr_listView);
		searchView = (EditText) getActivity().findViewById(
				R.id.favorite_scr_search_box);

		db.open();
		cursor = db.getAllFavoriteVendors();

		array_uids = new String[cursor.getCount()];
		array_Names = new String[cursor.getCount()];
		array_Emails = new String[cursor.getCount()];
		array_Phones = new String[cursor.getCount()];
		array_Countries = new String[cursor.getCount()];
		array_Cities = new String[cursor.getCount()];
		array_Addresses = new String[cursor.getCount()];
		array_PostalCodes = new String[cursor.getCount()];
		array_ImgLinks = new String[cursor.getCount()];
		array_WorkTimes = new String[cursor.getCount()];

		int v = 0;
		if (cursor.moveToFirst()) {
			do {

				array_uids[v] = cursor.getString(DBVendorHelper.COL_UID);
				array_Names[v] = cursor.getString(DBVendorHelper.COL_NAME);
				array_Emails[v] = cursor.getString(DBVendorHelper.COL_EMAIL);
				array_Phones[v] = cursor.getString(DBVendorHelper.COL_PHONE);
				array_Countries[v] = cursor
						.getString(DBVendorHelper.COL_COUNTRY);
				array_Cities[v] = cursor.getString(DBVendorHelper.COL_CITY);
				array_Addresses[v] = cursor
						.getString(DBVendorHelper.COL_ADDRESS);
				array_PostalCodes[v] = cursor
						.getString(DBVendorHelper.COL_POSTALCODE);
				array_ImgLinks[v] = cursor
						.getString(DBVendorHelper.COL_IMGLINK);
				array_WorkTimes[v] = cursor
						.getString(DBVendorHelper.COL_WORKTIME);
				v++;
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		displayListVendors.clear();

		for (int i = 0; i < array_Names.length; i++) {
			listVendors = new GlobalVariableSetting(array_uids[i],
					array_Names[i], array_Emails[i], array_Phones[i],
					array_Countries[i], array_Cities[i], array_Addresses[i],
					array_PostalCodes[i], array_ImgLinks[i], array_WorkTimes[i]);
			displayListVendors.add(listVendors);
		}

		vAdapter = new VendorsAdapter(getActivity(), R.layout.vendors_list,
				displayListVendors, array_Names);

		favorite_list.setAdapter(vAdapter);

		searchView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				GlobalVariableSetting templistVendors;

				textLength = searchView.getText().length();
				int t = 0;

				array_tempUids = new String[displayListVendors.size()];
				array_tempNames = new String[displayListVendors.size()];
				array_tempEmails = new String[displayListVendors.size()];
				array_tempPhones = new String[displayListVendors.size()];
				array_tempCountries = new String[displayListVendors.size()];
				array_tempCities = new String[displayListVendors.size()];
				array_tempAddresses = new String[displayListVendors.size()];
				array_tempPostalCodes = new String[displayListVendors.size()];
				array_tempImgLinks = new String[displayListVendors.size()];
				array_tempWorkTimes = new String[displayListVendors.size()];

				if (textLength == 0) {
					vAdapter = new VendorsAdapter(getActivity(),
							R.layout.vendors_list, displayListVendors,
							array_Names);
				} else {
					tempDispalyListVendors.clear();
					for (int i = 0; i < displayListVendors.size(); i++) {
						if (textLength <= displayListVendors.get(i).getvName()
								.length()) {
							if (searchView
									.getText()
									.toString()
									.equalsIgnoreCase(
											(String) displayListVendors.get(i)
													.getvName()
													.subSequence(0, textLength))) {

								array_tempUids[t] = displayListVendors.get(i)
										.getvUID();
								array_tempNames[t] = displayListVendors
										.get(i).getvName();
								array_tempEmails[t] = displayListVendors.get(
										i).getvEmail();
								array_tempPhones[t] = displayListVendors.get(
										i).getvPhonenumber();
								array_tempCountries[t] = displayListVendors
										.get(i).getvCountry();
								array_tempCities[t] = displayListVendors.get(
										i).getvCity();
								array_tempAddresses[t] = displayListVendors
										.get(i).getvAddress();
								array_tempPostalCodes[t] = displayListVendors
										.get(i).getvPostalCode();
								array_tempImgLinks[t] = displayListVendors
										.get(i).getvImgLink();
								array_tempWorkTimes[t] = displayListVendors
										.get(i).getvWorkTime();
								t++;
							}
						}

					}
					for (int i = 0; i < array_tempUids.length; i++) {

						templistVendors = new GlobalVariableSetting(
								array_tempUids[i], array_tempNames[i],
								array_tempEmails[i], array_tempPhones[i],
								array_tempCountries[i], array_tempCities[i],
								array_tempAddresses[i],
								array_tempPostalCodes[i],
								array_tempImgLinks[i], array_tempWorkTimes[i]);
						if (templistVendors.getvName() != null) {
							tempDispalyListVendors.add(templistVendors);
						}
					}
					vAdapter = new VendorsAdapter(getActivity(),
							R.layout.vendors_list, tempDispalyListVendors,
							array_tempNames);
				}
				favorite_list.setAdapter(vAdapter);
			}
		});
		
		favorite_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long index) {
				Intent detail = new Intent(getActivity(), DetailActivity.class);
				
				if (textLength != 0) {

					detail.putExtra("name", tempDispalyListVendors
							.get(position).getvName());
					detail.putExtra("uid", tempDispalyListVendors.get(position)
							.getvUID());
					detail.putExtra("name", tempDispalyListVendors
							.get(position).getvName());
					detail.putExtra("email",
							tempDispalyListVendors.get(position).getvEmail());
					detail.putExtra("phone",
							tempDispalyListVendors.get(position)
									.getvPhonenumber());
					detail.putExtra("country",
							tempDispalyListVendors.get(position).getvCountry());
					detail.putExtra("city", tempDispalyListVendors
							.get(position).getvCity());
					detail.putExtra("address",
							tempDispalyListVendors.get(position).getvAddress());
					detail.putExtra("postalcode",
							tempDispalyListVendors.get(position)
									.getvPostalCode());
					detail.putExtra("imgLink",
							tempDispalyListVendors.get(position).getvImgLink());
					detail.putExtra("workTime",
							tempDispalyListVendors.get(position).getvWorkTime());

				} else {

					detail.putExtra("uid", displayListVendors.get(position)
							.getvUID());
					detail.putExtra("name", displayListVendors.get(position)
							.getvName());
					detail.putExtra("email", displayListVendors.get(position)
							.getvEmail());
					detail.putExtra("phone", displayListVendors.get(position)
							.getvPhonenumber());
					detail.putExtra("country", displayListVendors.get(position)
							.getvCountry());
					detail.putExtra("city", displayListVendors.get(position)
							.getvCity());
					detail.putExtra("address", displayListVendors.get(position)
							.getvAddress());
					detail.putExtra("postalcode",
							displayListVendors.get(position).getvPostalCode());
					detail.putExtra("imgLink", displayListVendors.get(position)
							.getvImgLink());
					detail.putExtra("workTime", displayListVendors
							.get(position).getvWorkTime());
				}
				detail.putExtra("activity", "favoritelist");
				
				detail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(detail);
				getActivity().finish();
			}
		});

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
