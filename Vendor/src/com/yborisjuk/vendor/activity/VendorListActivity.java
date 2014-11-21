package com.yborisjuk.vendor.activity;

import java.util.ArrayList;
import java.util.List;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.lib.adapters.VendorsAdapter;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;
import com.yborisjuk.vendor.libs.mySharedPreferences;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class VendorListActivity extends Activity {

	private ListView vendor_list;
	private EditText search_box;
	private VendorsAdapter vAdapter;
	private GlobalVariableSetting listVendors;
	private List<GlobalVariableSetting> displayListVendors = new ArrayList<GlobalVariableSetting>();
	private List<GlobalVariableSetting> tempDispalyListVendors = new ArrayList<GlobalVariableSetting>();
	private String[] array_uids, array_Names, array_Emails, array_Phones,
			array_Countries, array_Cities, array_Addresses, array_PostalCodes,
			array_ImgLinks, array_WorkTimes;
	private String[] array_tempUids, array_tempNames, array_tempEmails,
			array_tempPhones, array_tempCountries, array_tempCities,
			array_tempAddresses, array_tempPostalCodes, array_tempImgLinks,
			array_tempWorkTimes;
	private GlobalVariableSetting gvs = new GlobalVariableSetting();
	private mySharedPreferences sharedPreferences;
	private int textLength;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vendor_screen);

		vendor_list = (ListView) findViewById(R.id.vendor_scr_list);
		search_box = (EditText) findViewById(R.id.vendor_scr_search_box);

		array_uids = gvs.getArray_vUID();
		array_Names = gvs.getArray_vName();
		array_Emails = gvs.getArray_vEmail();
		array_Phones = gvs.getArray_vPhone();
		array_Countries = gvs.getArray_vCountry();
		array_Cities = gvs.getArray_vCity();
		array_Addresses = gvs.getArray_vAddress();
		array_PostalCodes = gvs.getArray_vPostalCode();
		array_ImgLinks = gvs.getArray_vImgLink();
		array_WorkTimes = gvs.getArray_vWorkTime();

		for (int i = 0; i < array_uids.length; i++) {
			listVendors = new GlobalVariableSetting(array_uids[i],
					array_Names[i], array_Emails[i], array_Phones[i],
					array_Countries[i], array_Cities[i], array_Addresses[i],
					array_PostalCodes[i], array_ImgLinks[i], array_WorkTimes[i]);
			displayListVendors.add(listVendors);
		}

		vAdapter = new VendorsAdapter(getApplicationContext(),
				R.layout.vendors_list, displayListVendors, array_Names);

		vendor_list.setAdapter(vAdapter);

		search_box.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable cs) {

				GlobalVariableSetting templistVendors;

				textLength = search_box.getText().length();
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
					vAdapter = new VendorsAdapter(getApplicationContext(),
							R.layout.vendors_list, displayListVendors,
							array_Names);
				} else {
					tempDispalyListVendors.clear();
					for (int i = 0; i < displayListVendors.size(); i++) {
						if (textLength <= displayListVendors.get(i).getvName()
								.length()) {
							if (search_box
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
					vAdapter = new VendorsAdapter(getApplicationContext(),
							R.layout.vendors_list, tempDispalyListVendors,
							array_tempNames);
				}
				vendor_list.setAdapter(vAdapter);
			}
		});

		vendor_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long index) {

				Intent detail = new Intent(getApplicationContext(),
						DetailActivity.class);

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
					detail.putExtra("activity", "vendorlist");
				detail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(detail);
				finish();

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.additional_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == R.id.logout) {
			sharedPreferences = new mySharedPreferences(getApplicationContext());
			sharedPreferences.clearPrefence();

			Intent loginIntent = new Intent(getApplicationContext(),
					LoginActivity.class);
			loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(loginIntent);
			finish();
		} else if (id == R.id.back) {
			Intent homeIntent = new Intent(getApplicationContext(),
					MainActivity.class);
			homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(homeIntent);
			finish();
		}
		return super.onOptionsItemSelected(item);
	}
}
