package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
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
				String contentEncoded = item.getContentEncoded();
				String pubdate = item.getPubdate();
				if(contentEncoded != null && contentEncoded.length() != 0)
				{
					intent.putExtra("item_detail", contentEncoded);
				}
				else
				{ 
					intent.putExtra("item_detail", items.get(position).getDescription());
				}
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				
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
				item.setTitle(title);
				item.setPubdate(pubdate);
				item.setDescription(itemDetail);
				items.add(item);
				cursor.moveToNext();
			}
		}
		mAdapter = new ItemListAdapter(this, items, false);
		favoriteLv.setAdapter(mAdapter);
		db.close();
	}
}
