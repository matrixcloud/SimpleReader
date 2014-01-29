package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.database.DataSetObserver;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class MPagerAdapter extends PagerAdapter
{
	public static final String tag = "MPagerAdapter";
	private ArrayList<GridView> views;
	
	
	public MPagerAdapter(ArrayList<GridView> views)
	{
		this.views = views;
	}
	
//	@Override
//	public void destroyItem(View container, int position, Object object)
//	{
//		Log.i(tag, "--------->>destroyItem()");
//		super.destroyItem(container, position, object);
//	}
//
//	@Override
//	public void finishUpdate(View container)
//	{
//		Log.i(tag, "------------->>finishUpdate()--view");
//		super.finishUpdate(container);
//	}
//
//	@Override
//	public void finishUpdate(ViewGroup container)
//	{
//		Log.i(tag, "------------->>finishUpdate()--ViewGroup");
//		super.finishUpdate(container);
//	}
//
//	@Override
//	public int getItemPosition(Object object)
//	{
//		Log.i(tag, "--------------->>getItemPosition()");
//		return super.getItemPosition(object);
//	}
//
//	@Override
//	public CharSequence getPageTitle(int position)
//	{
//		Log.i(tag, "------------->>getPageTitle()");
//		return super.getPageTitle(position);
//	}
//
//	@Override
//	public float getPageWidth(int position)
//	{
//		Log.i(tag, "------------->>getPageWidth()");
//		return super.getPageWidth(position);
//	}
//
//	@Override
//	public void notifyDataSetChanged()
//	{
//		Log.i(tag, "----------->>notifyDataSetChanged()");
//		super.notifyDataSetChanged();
//	}
//
//	@Override
//	public void registerDataSetObserver(DataSetObserver observer)
//	{
//		super.registerDataSetObserver(observer);
//	}
//
//	@Override
//	public void restoreState(Parcelable state, ClassLoader loader)
//	{
//		Log.i(tag, "-------------->>restoreState()");
//		super.restoreState(state, loader);
//	}
//
//	@Override
//	public Parcelable saveState()
//	{
//		Log.i(tag, "------------>>saveState()");
//		return super.saveState();
//	}
//
//	@Override
//	public void setPrimaryItem(View container, int position, Object object)
//	{
//		super.setPrimaryItem(container, position, object);
//	}
//
//	@Override
//	public void setPrimaryItem(ViewGroup container, int position, Object object)
//	{
//		super.setPrimaryItem(container, position, object);
//	}
//
//	@Override
//	public void startUpdate(View container)
//	{
//		Log.i(tag, "--------->>startUpdate()--view");
//		super.startUpdate(container);
//	}
//
//	@Override
//	public void startUpdate(ViewGroup container)
//	{
//		Log.i(tag, "------------>>startUpdate()--viewGroup");
//		super.startUpdate(container);
//	}
//
//	@Override
//	public void unregisterDataSetObserver(DataSetObserver observer)
//	{
//		super.unregisterDataSetObserver(observer);
//	}
//
//	@Override
//	public void destroyItem(ViewGroup container, int position, Object object)
//	{
//		Log.i(tag, "-------------->>destroyItem()");
//		container.removeView(views.get(position));
//	}

	@Override
	public Object instantiateItem(ViewGroup container, int position)
	{
		Log.i(tag, "instantiateItem()---container, position");
		Log.i(tag, container + "");
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
