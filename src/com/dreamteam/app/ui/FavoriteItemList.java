package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.dreamteam.app.adapter.ItemListAdapter;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.entity.FeedItem;
import com.dreateam.app.ui.R;

/**
 * @description
 * @author zcloud
 * @date Nov 28, 2013
 */
public class FavoriteItemList extends Activity
{
	private ListView favoriteLv;
	private ItemListAdapter mAdapter;
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private Intent intent = new Intent();
	private String sectionUrl;//暂用
	private BroadcastReceiver mReceiver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initBroadCast();
	}

	private void initView()
	{
		setContentView(R.layout.favorite_list);
		favoriteLv = (ListView) findViewById(R.id.favorite_list);
		favoriteLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				FeedItem item = items.get(position);
				String title = item.getTitle();
				String content = item.getContent();
				String pubdate = item.getPubdate();
				String link = item.getLink();
				if(content != null && content.length() != 0)
				{
					intent.putExtra("item_detail", content);
				}
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				intent.putExtra("is_favorite", true);
				intent.putExtra("link", link);
				intent.putExtra("section_url", sectionUrl);
				
				intent.setClass(FavoriteItemList.this, ItemDetail.class);
				FavoriteItemList.this.startActivity(intent);
			}
		});
		findViewById(R.id.favorite_list_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
	}
	
	private void initData()
	{
		DbManager mgr = new DbManager(this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbManager.FAVORITE_ITEM_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.moveToFirst())
		{
			for(int i = 0, n = cursor.getCount(); i < n; i++)
			{
				FeedItem item = new FeedItem();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String pubdate = cursor.getString(cursor.getColumnIndex("pubdate"));
				String itemDetail = cursor.getString(cursor.getColumnIndex("item_detail"));
				String link = cursor.getString(cursor.getColumnIndex("link"));
				
				sectionUrl = cursor.getString(cursor.getColumnIndex("table_url"));
				
				item.setTitle(title);
				item.setPubdate(pubdate);
				item.setContent(itemDetail);
				item.setFavorite(true);
				item.setLink(link);
				
				items.add(item);
				cursor.moveToNext();
			}
		}
		mAdapter = new ItemListAdapter(this, items, false);
		favoriteLv.setAdapter(mAdapter);
		cursor.close();
		db.close();
	}
	
	private void initBroadCast()
	{
		mReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent) {
				String link = intent.getStringExtra("link");
				for(int i = 0; i < items.size(); i++)
				{
					FeedItem item = items.get(i);
					if(item.getLink().equals(link))
					{
						items.remove(i);
						mAdapter.notifyDataSetChanged();
						return;
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ItemList.ACTION_UPDATE_ITEM_LIST);
		registerReceiver(mReceiver, filter);
	}
	
	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}

}
