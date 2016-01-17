package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class MPagerAdapter extends PagerAdapter
{
	@Override
	public void destroyItem(ViewGroup container, int position, Object object)
	{
		((ViewPager) container).removeView(views.get(position));
	}

	public static final String tag = "MPagerAdapter";
	private ArrayList<GridView> views;
	
	
	public MPagerAdapter(ArrayList<GridView> views)
	{
		this.views = views;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		((ViewPager) container).addView(views.get(position), 0);
		return views.get(position);
	}

	@Override
	public int getCount()
	{
		return views.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1)
	{
		return arg0 == arg1;
	}

	public void removeAllViews()
	{
		for(int i = 0; i < views.size(); i++)
		{
			views.remove(i);
		}
		notifyDataSetChanged();
	}
	
}
