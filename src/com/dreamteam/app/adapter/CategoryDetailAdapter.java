package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.dreamteam.app.db.DBHelper;
import com.dreamteam.app.db.FeedDBHelper;
import com.dreamteam.app.entity.Feed;
import com.dreamteam.app.ui.Main;
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
	private ArrayList<Feed> feeds;
	private String tableName;//所分类对应的表名
	public static final String SECTION_TABLE_NAME = "section";

	public CategoryDetailAdapter(Context context, ArrayList<Feed> feeds, String tableName)
	{
		this.context = context;
		this.feeds = feeds;
		this.tableName = tableName;
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
	public Object getItem(int position)
	{
		return feeds.get(position);
	}

	@Override
	public long getItemId(int id)
	{
		return id;
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
			holder.feedTitle = (TextView) convertView.findViewById(R.id.category_detail_feed_title);
			holder.addBtn = (ImageButton) convertView.findViewById(R.id.category_detail_add);
			holder.addBtn.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					Feed feed = feeds.get(position);
					Intent intent = new Intent();
					
					//已经选中，取消选中状态
					if(feed.getSelectStatus() == 1)
					{
						
						//该变传入feeds
						feed.setSelectStatus(0);
						Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
								R.drawable.add);
						holder.addBtn.setImageBitmap(bmp);
						//更新主界面
						intent.putExtra("url", feed.getUrl());
						intent.setAction(Main.DELETE_SECTION);
						context.sendBroadcast(intent);
						//删除section表中记录的数据
						DBHelper helper = new DBHelper(context, DBHelper.DB_NAME, null, 1);
						SQLiteDatabase db = helper.getWritableDatabase();
						db.delete(SECTION_TABLE_NAME, "url=?", new String[]{feed.getUrl()});
						db.close();
						//更新feed.db中所对应表的状态为0
						FeedDBHelper helper_1 = new FeedDBHelper(context, FeedDBHelper.DB_NAME, null, 1);
						SQLiteDatabase db_1 = helper_1.getWritableDatabase();
						ContentValues values = new ContentValues();
						values.put("select_status", 0);
						db_1.update(tableName, values, "url=?", new String[]{feed.getUrl()});
						db_1.close();
						return;
					}
					//否则，选中状态
					feed.setSelectStatus(1);
					Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
							R.drawable.added);
					holder.addBtn.setImageBitmap(bmp);
					//更新主界面
					intent.setAction(Main.ADD_SECTION);
					context.sendBroadcast(intent);
					//加入section表
					DBHelper helper = new DBHelper(context, DBHelper.DB_NAME, null, 1);
					SQLiteDatabase db = helper.getWritableDatabase();
					ContentValues values = new ContentValues();
					values.put("title", feed.getTitle());
					values.put("url", feed.getUrl());
					values.put("table_name", tableName);
					db.insert(SECTION_TABLE_NAME, null, values);
					db.close();
					//更新feed.db中所对应表的状态为1
					FeedDBHelper helper_1 = new FeedDBHelper(context, FeedDBHelper.DB_NAME, null, 1);
					SQLiteDatabase db_1 = helper_1.getWritableDatabase();
					ContentValues values_1 = new ContentValues();
					values_1.put("select_status", 1);
					db_1.update(tableName, values_1, "url=?", new String[]{feed.getUrl()});
					db_1.close();
				}
			});
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		Feed feed = feeds.get(position);
		holder.feedTitle.setText((CharSequence)
				feed.getTitle());
		//addBtn状态图标设置
		Bitmap bm = null;
		if(feed.getSelectStatus() == 1)
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
		holder.addBtn.setImageBitmap(bm);
		return convertView;
	}
	
	
	private static final class ViewHolder
	{
		TextView feedTitle;
		ImageButton addBtn;
	}
	
}
