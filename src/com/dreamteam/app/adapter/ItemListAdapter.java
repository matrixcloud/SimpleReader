/**
 * 
 */
package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.utils.ImageLoader;
import com.dreateam.app.ui.R;

/**
 * @author:	zcloud
 *
 * @description: TODO
 *
 * @date: 2013/10/14
 *
 */
public class ItemListAdapter extends BaseAdapter
{
	public static final String tag = "ItemListAdapter";
	private LayoutInflater inflater;
	private ArrayList<FeedItem> items;
	private ArrayList<String> imageUrls = new ArrayList<String>();
	private ImageLoader loader = new ImageLoader();
	private static int[] colors;//是否已阅读显示的颜色
	private boolean loadImg = false;
	
	public ItemListAdapter(Context context, ArrayList<FeedItem> items, boolean isNight)
	{
		this.items = items;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Bitmap defBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_default);
		loader.setDefBitmap(defBitmap);
		Resources res = context.getResources();
		colors = new int[]{
				res.getColor(R.color.black),
				res.getColor(R.color.dark_gray)
		};
		if(isNight)
		{
			colors = new int[]{
					res.getColor(R.color.white),
					res.getColor(R.color.gray)
			};
		}
		//是否加载图片
		SharedPreferences prefs = AppContext.getPrefrences(context);
		loadImg = prefs.getBoolean("pref_imageLoad", false);
	}
	
	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public FeedItem getItem(int position)
	{
		return items.get(position);
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
		int colorState = 0;
		
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.feed_item, null);
			viewHolder = new ViewHolder();
			viewHolder.itemIv = (ImageView) convertView.findViewById(R.id.feeditem_image);
			viewHolder.titleTv = (TextView) convertView.findViewById(R.id.feeditem_title);
			viewHolder.pubdateTv = (TextView) convertView.findViewById(R.id.feeditem_pubdate);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		FeedItem item = items.get(position);
		String title = item.getTitle();
		if(title.length() > 30)
			title = title.substring(0, 30);
		if(item.isReaded())
			colorState = 1;
//		else
//			colorState = 0;
		viewHolder.titleTv.setTextColor(colors[colorState]);
		viewHolder.titleTv.setText(title);
		viewHolder.pubdateTv.setText(item.getPubdate());
		imageUrls = item.getImageUrls();
		if(imageUrls.isEmpty() || !loadImg)
		{
			viewHolder.itemIv.setVisibility(View.GONE);
		}
		else
		{
			loader.loadImage(imageUrls.get(0), viewHolder.itemIv, 100, viewHolder.titleTv.getHeight());
		}
		return convertView;
	}

	private static final class ViewHolder
	{
		ImageView itemIv;
		TextView titleTv;
		TextView pubdateTv;
	}

	public void addItemsToHead(ArrayList<FeedItem> newItems)
	{
		items.addAll(0, newItems);
		notifyDataSetChanged();
	}
}
