package com.dreateam.app.adpater;

import java.util.ArrayList;

import com.dreateam.app.ui.MFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MPagerAdapter extends FragmentStatePagerAdapter
{
	private ArrayList<MFragment> fragments = new ArrayList<MFragment>();
	
	
	public MPagerAdapter(FragmentManager fm, ArrayList<MFragment> fragments)
	{
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int postion)
	{
		return fragments.get(postion);
	}

	@Override
	public int getCount()
	{
		return fragments.size();
	}

}
