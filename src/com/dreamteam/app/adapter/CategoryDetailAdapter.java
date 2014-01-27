package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreamteam.app.db.FeedDBHelper;
import com.dreamteam.app.entity.Feed;
import com.dreamteam.app.entity.Section;
import com.dreateam.app.ui.R;

/**
 * @description TODO
 * @author zcloud
 * @date 2013/11/14
 */
public class CategoryDetailAdapter extends BaseAdapter
{
	public static final String tag = "CategoryDetailAdapter";
	private LayoutInflater inflater;
	
	private Context context;
	
	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	private String tableName;
	

	public CategoryDetailAdapter(Context context)
	{
		this.context = context;
	}
	
	public CategoryDetailAdapter(Context context, ArrayList<Feed> feeds)
	{
		this.context = context;
		this.feeds = feeds;
	}
	
	public void updateData(ArrayList<Feed> feeds)
	{
		this.feeds = feeds;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount()
	{
		return feeds.size();
	}

	@Override
	public Object getItem(int arg0)
	{
		return null;
	}

	@Override
	public long getItemId(int arg0)
	{
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		final ViewHolder holder;
		
		if(convertView == null)
		{
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.category_detail_item, null);
			holder = new ViewHolder();
			holder.FEED_TITLE = (TextView) convertView.findViewById(R.id.category_detail_feed_title);
			holder.ADD_BTN = (ImageButton) convertView.findViewById(R.id.category_detail_add);
			holder.ADD_BTN.setOnClickListener(new OnClickListener()
			{
				/* (non-Javadoc)
				 * @see android.view.View.OnClickListener#onClick(android.view.View)
				 */
				@Override
				public void onClick(View v)
				{
					if(feeds.get(position).getSelectStatus() == 1)
						return;
					Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.added);
					holder.ADD_BTN.setImageBitmap(bmp);

					FeedDBHelper helper = new FeedDBHelper(context, "feed.db", null, 1);
					SQLiteDatabase db = helper.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put("select_status", 1);
					db.update(tableName, values, "id=?", new String[]{"" + position});
					db.close();
					
					String title = feeds.get(position).getTitle();
					String url = feeds.get(position).getUrl();
					Section s = new Section();
					s.setTitle(title);
					s.setUrl(url);
					s.setTableName(tableName);
				}
			});
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Feed f = feeds.get(position);
		holder.FEED_TITLE.setText((CharSequence) f.getTitle());
		Bitmap bm = null;
		if(f.getSelectStatus() == 1)
		{
			bm = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.added);
			notifyDataSetChanged();
		}
		else
		{
			bm =  BitmapFactory.decodeResource(context.getResources(),
					R.drawable.add);
			notifyDataSetChanged();
		}
		holder.ADD_BTN.setImageBitmap(bm);
		return convertView;
	}
	private static final class ViewHolder
	{
		TextView FEED_TITLE;
		ImageButton ADD_BTN;
	}
	public void setTabelName(String tableName)
	{
		this.tableName = tableName;
	}
	
}
