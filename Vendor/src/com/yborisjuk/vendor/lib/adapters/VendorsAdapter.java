package com.yborisjuk.vendor.lib.adapters;

import java.util.List;

import com.yborisjuk.vendor.R;
import com.yborisjuk.vendor.libs.GlobalVariableSetting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VendorsAdapter extends ArrayAdapter<GlobalVariableSetting> {

	private Context context;
	private List<GlobalVariableSetting> displayListVendors;
	private LayoutInflater inflater;
	private String[] array_Names;

	public VendorsAdapter(Context context, int resourceId,
			List<GlobalVariableSetting> displayListVendors, String[] array_Names) {

		super(context, resourceId, displayListVendors);
		this.context = context;
		this.displayListVendors = displayListVendors;
		this.array_Names = array_Names;
		inflater = LayoutInflater.from(context);
	}

	private class ViewHolder {
		TextView vName;
	}

	public View getView(final int position, View view, ViewGroup parent) {

		final ViewHolder holder;

		if (view == null) {

			holder = new ViewHolder();
			view = inflater.inflate(R.layout.vendors_list, null);
			holder.vName = (TextView) view.findViewById(R.id.vendor_ls_vName);
			view.setTag(holder);

		} else {

			holder = (ViewHolder) view.getTag();
		}

		holder.vName.setText(displayListVendors.get(position).getvName());

		return view;
	}
}
