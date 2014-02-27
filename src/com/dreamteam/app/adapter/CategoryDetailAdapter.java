package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FeedDBManager;
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
	private int[] imgIds = {
			R.drawable.add,
			R.drawable.added
	};
	
	
	public CategoryDetailAdapter(Context context, ArrayList<Feed> feeds, String tableName)
	{
		this.context = context;
		this.feeds = feeds;
		this.tableName = tableName;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
			convertView = inflater.inflate(R.layout.category_detail_item, null);
			holder = new ViewHolder();
			holder.feedTitle = (TextView) convertView.findViewById(R.id.category_detail_feed_title);
			holder.addBtn = (ImageButton) convertView.findViewById(R.id.category_detail_add);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.addBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Feed feed = feeds.get(position);
				String title = feed.getTitle();
				String url = feed.getUrl();
				Intent intent = new Intent();
				int state = 0;//初始未选中
				DbManager mgr = new DbManager(context, DbManager.DB_NAME, null, 1);

				
				//已经选中，取消选中状态
				if(feed.isSelected())
				{
					//改变传入feeds
					feed.setSelectStatus(state);
					holder.addBtn.setImageResource(imgIds[0]);
					//更新主界面
					intent.putExtra("url", feed.getUrl());
					intent.setAction(Main.ACTION_DELETE_SECTION);
					context.sendBroadcast(intent);
					//删除section表中记录的数据
					SectionHelper.removeRecord(mgr.getWritableDatabase(), url);
					//更新feed.db中所对应表的状态为0
					new FeedDBManager(context, FeedDBManager.DB_NAME, null, 1)
								.updateState(tableName, state, url);
					return;
				}
				//否则，选中状态
				state = 1;
				feed.setSelectStatus(state);
				holder.addBtn.setImageResource(imgIds[1]);
				//更新主界面
				intent.setAction(Main.ACTION_ADD_SECTION);
				context.sendBroadcast(intent);
				//加入section表
				SQLiteDatabase db = mgr.getWritableDatabase();
				SectionHelper.insert(db, tableName, title, url);
				db.close();
				//更新feed.db中所对应表的状态为1
				FeedDBManager feedHelper = new FeedDBManager(context, FeedDBManager.DB_NAME, null, 1);
				feedHelper.updateState(tableName,state, url);
			}
		});
		Feed feed = feeds.get(position);
		holder.feedTitle.setText((CharSequence)
				feed.getTitle());
		//addBtn状态图标设置
		holder.addBtn.setImageResource(imgIds[feed.getSelectStatus()]);
		return convertView;
	}
	
	private static final class ViewHolder
	{
		TextView feedTitle;
		ImageButton addBtn;
	}
	
}
