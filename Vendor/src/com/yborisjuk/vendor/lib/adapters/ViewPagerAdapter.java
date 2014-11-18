package com.yborisjuk.vendor.lib.adapters;

import java.util.List;

import com.yborisjuk.vendor.libs.GlobalVariableSetting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
	private FragmentManager context;
	private List<GlobalVariableSetting> imgList;
	private String[] array_imgLink;
	private int mCount;

	public ViewPagerAdapter(FragmentManager context,
			List<GlobalVariableSetting> imgList, String[] array_imgLink) {
		super(context);
		this.context = context;
		this.imgList = imgList;
		this.array_imgLink = array_imgLink;
		mCount = imgList.size();
	}

	public ViewPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return ViewFragment.newInstance(array_imgLink[position]);
	}

	@Override
	public int getCount() {
		return mCount;
	}
	

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

}
