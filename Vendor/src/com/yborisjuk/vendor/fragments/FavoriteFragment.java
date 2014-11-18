package com.yborisjuk.vendor.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.activity.DetailActivity;
import com.yborisjuk.vendor.lib.adapters.VendorsAdapter;
import com.yborisjuk.vendor.libs.DBVendorHelper;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;

public class FavoriteFragment extends Fragment {

	private DBVendorHelper db;
	private ListView favorite_list;
	private EditText searchView;
	private GlobalVariableSetting listVendors;
	private VendorsAdapter vAdapter;
	private List<GlobalVariableSetting> displayListVendors = new ArrayList<GlobalVariableSetting>();
	private String[] array_uids, array_Names, array_Emails, array_Phones, array_Countries,
			array_Cities, array_Addresses, array_PostalCodes, array_ImgLinks,
			array_WorkTimes;
	private Cursor cursor;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.favorite_screen_frg, container, false);
		return view;
	};
	
	@Override
	public void onStart() {
		super.onStart();
		
		db = new DBVendorHelper(getActivity());
		favorite_list = (ListView) getActivity().findViewById(R.id.favorite_scr_listView);
		searchView = (EditText) getActivity().findViewById(R.id.favorite_scr_search_box);
		
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
		
		int v1 = 0, v2 = 0, v3 = 0, v4 = 0, v5 = 0, v6 = 0, v7 = 0, v8 = 0, v9 = 0, v10 = 0;
		if (cursor.moveToFirst()) {
			do {
				
				array_uids[v1++] = cursor.getString(DBVendorHelper.COL_UID);
				array_Names[v2++] = cursor.getString(DBVendorHelper.COL_NAME);
				array_Emails[v3++] = cursor.getString(DBVendorHelper.COL_EMAIL);
				array_Phones[v4++] = cursor.getString(DBVendorHelper.COL_PHONE);
				array_Countries[v5++] = cursor.getString(DBVendorHelper.COL_COUNTRY);
				array_Cities[v6++] = cursor.getString(DBVendorHelper.COL_CITY);
				array_Addresses[v7++] = cursor.getString(DBVendorHelper.COL_ADDRESS);
				array_PostalCodes[v8++] = cursor.getString(DBVendorHelper.COL_POSTALCODE);
				array_ImgLinks[v9++] = cursor.getString(DBVendorHelper.COL_IMGLINK);
				array_WorkTimes[v10++] = cursor.getString(DBVendorHelper.COL_WORKTIME);
				
			} while(cursor.moveToNext());
		}
		cursor.close();
		db.close();
		displayListVendors.clear();
		
		for (int i = 0; i < array_Names.length; i++) {
			listVendors = new GlobalVariableSetting(array_uids[i], array_Names[i],
					array_Emails[i], array_Phones[i], array_Countries[i],
					array_Cities[i], array_Addresses[i], array_PostalCodes[i],
					array_ImgLinks[i],array_WorkTimes[i]);
			displayListVendors.add(listVendors);
		}
		
		vAdapter = new VendorsAdapter(getActivity(),
				R.layout.vendors_list, displayListVendors, array_Names);
		
		favorite_list.setAdapter(vAdapter);
		favorite_list.setTextFilterEnabled(true);
		
		favorite_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long index) {
				Intent detail = new Intent(getActivity(),
						DetailActivity.class);
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
				detail.putExtra("postalcode", displayListVendors.get(position)
						.getvPostalCode());
				detail.putExtra("imgLink", displayListVendors.get(position)
						.getvImgLink());
				detail.putExtra("workTime", displayListVendors.get(position)
						.getvWorkTime());

				detail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(detail);
				getActivity().finish();
			}
		});
		
	}
}

