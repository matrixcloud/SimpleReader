package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.dreamteam.app.ui.MFragment;

public class MPagerAdapter extends FragmentStatePagerAdapter
{
	public static final String tag = "MPagerAdapter";
	private ArrayList<MFragment> fragments = null;
	
	
	public MPagerAdapter(FragmentManager fm, ArrayList<MFragment> fragments)
	{
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int postion)
	{
		MFragment fragment = fragments.get(postion);
		Bundle args = new Bundle();
		args.putInt("page", postion);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public int getCount()
	{
		return fragments.size();
	}
	
	public void addItem(MFragment fragment)
	{
		fragments.add(fragment);
		notifyDataSetChanged();
	}
	
	public void removeItem(int index)
	{
		fragments.remove(index);
		notifyDataSetChanged();
	}
	
	public MFragment getLastFragment()
	{
		if(fragments.isEmpty())
			return null;
		return fragments.get(fragments.size() - 1);
	}

	public void removeLastItem()
	{
		if(fragments.isEmpty())
			return;
		fragments.remove(fragments.size() - 1);
		notifyDataSetChanged();
	}
	
	//保证至少有一个fragment
	public boolean isOneLesser()
	{
		return fragments.size() < 1;
	}
	
	public MFragment getFragment(int position)
	{
		return fragments.get(position);
	}
}
