package com.dreamteam.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreateam.app.ui.R;

/**
 * @description 
 * @author zcloud
 * @date 2013年11月1日
 */
public class FeedCategoryAdapter extends BaseAdapter
{
	private Context context;
	private LayoutInflater inflater;
	private String[] categories_zh;
	
	
	public FeedCategoryAdapter(Context context)
	{
		this.context = context;
		categories_zh = context.getResources()
				   .getStringArray(R.array.feed_category);
	}	
	
	@Override
	public int getCount()
	{
		return categories_zh.length;
	}

	@Override
	public Object getItem(int position)
	{
		return categories_zh[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder viewHolder = null;
		
		if(convertView == null)
		{
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.category_item, null);
			viewHolder = new ViewHolder();
			viewHolder.categoryTitle = (TextView) convertView.findViewById(R.id.category_title);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.categoryTitle.setText(categories_zh[position]);
		return convertView;
	}
	
	private static final class ViewHolder
	{
		TextView categoryTitle;
	}
}
