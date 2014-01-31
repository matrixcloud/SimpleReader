/**
 * 
 */
package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
	private Context context;
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private ArrayList<String> imageUrls = new ArrayList<String>();
	private ImageLoader loader = new ImageLoader();
	
	
	public ItemListAdapter(Context context)
	{
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		Bitmap defBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading_default);
		loader.setDefBitmap(defBitmap);
	}

	public void updateData(ArrayList<FeedItem> items)
	{
		this.items = items;
		notifyDataSetChanged();
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
		
		if(convertView == null)
		{
			convertView = inflater.inflate(R.layout.feed_item, null);
			viewHolder = new ViewHolder();
			viewHolder.ITEM_IMAGE = (ImageView) convertView.findViewById(R.id.feeditem_image);
			viewHolder.ITEM_TITLE = (TextView) convertView.findViewById(R.id.feeditem_title);
			viewHolder.ITEM_PUBDATE = (TextView) convertView.findViewById(R.id.feeditem_pubdate);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		FeedItem item = items.get(position);
		String title = item.getTitle();
		if(title.length() > 24)
			title = title.substring(0, 21) + "...";
		viewHolder.ITEM_TITLE.setText(title);
		imageUrls = item.getImageUrls();
		if(imageUrls.isEmpty())
		{
			viewHolder.ITEM_IMAGE.setVisibility(View.GONE);
		}
		else
		{
			loader.loadImage(imageUrls.get(0), viewHolder.ITEM_IMAGE, 100, viewHolder.ITEM_TITLE.getHeight());
		}
		viewHolder.ITEM_PUBDATE.setText(item.getPubdate());
		
		return convertView;
	}

	public ArrayList<FeedItem> getItems()
	{
		return items;
	}

	public void setItems(ArrayList<FeedItem> items)
	{
		this.items = items;
	}

	private static final class ViewHolder
	{
		ImageView ITEM_IMAGE;
		TextView ITEM_TITLE;
		TextView ITEM_PUBDATE;
	}

	public void addItems(ArrayList<FeedItem> newItems)
	{
		items.addAll(newItems);
		notifyDataSetChanged();
	}
}
