package com.dreamteam.app.ui; 

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.ItemListAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SerializationHelper;
import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.app.utils.FileUtils;
import com.dreamteam.custom.ui.PullToRefreshListView;
import com.dreamteam.custom.ui.PullToRefreshListView.OnRefreshListener;
import com.dreateam.app.ui.R;

/**
 * @description TODO
 * @author zcloud
 * @date 2013/11/14
 */
public class ItemList extends Activity
{
	public static final String tag = "ItemList";
	
	private PullToRefreshListView itemLv;
	private ImageButton backBtn;
	private TextView feedTitleTv;
	private ItemListAdapter mAdapter;
	private ArrayList<FeedItem> mItems = new ArrayList<FeedItem>();
	private Intent intent;
	private String sectionTitle; 
	private String sectionUrl;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}


	private void initView()
	{
		setContentView(R.layout.feed_item_list);
		
		backBtn = (ImageButton) findViewById(R.id.fil_back_btn);
		backBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				ItemList.this.finish();
			}
		});
		
		feedTitleTv = (TextView) findViewById(R.id.fil_feed_title);
		itemLv = (PullToRefreshListView) findViewById(R.id.fil_lv_feed_item);
		mAdapter = new ItemListAdapter(this);
		
		itemLv.setAdapter(mAdapter);
		
		itemLv.setOnRefreshListener(new OnRefreshListener()
		{
			public void onRefresh()
			{
				if(!AppContext.isNetworkAvailable(ItemList.this))
				{
					itemLv.onRefreshComplete();
					Toast.makeText(ItemList.this, R.string.no_network, Toast.LENGTH_SHORT).show();
					return;
				}
				new RefreshTask().execute(sectionUrl);
			}
		});
		itemLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent();
				FeedItem item = mItems.get(position - 1);
				String title = item.getTitle();
				String contentEncoded = item.getContentEncoded();
				String pubdate = item.getPubdate();
				if(contentEncoded != null && contentEncoded.length() != 0)
				{
					intent.putExtra("item_detail", contentEncoded);
				}
				else
				{
					intent.putExtra("item_detail", mItems.get(position - 1).getDescription());
				}
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				intent.putExtra("section_title", sectionTitle);
				intent.setClass(ItemList.this, ItemDetail.class);
				ItemList.this.startActivity(intent);
			}
			
		});
	}

	private void initData()
	{
		intent = getIntent();
		sectionTitle = intent.getStringExtra("section_title") + "";
		sectionUrl = intent.getStringExtra("url");
		feedTitleTv.setText(sectionTitle);
		
		File file = FileUtils.getSectionCacheFile(sectionUrl);
		if(file.exists())
		{
			SerializationHelper seriaHelper = SerializationHelper.newInstance();
			ItemListEntity itemListEntity = (ItemListEntity) seriaHelper.readObject(file);
			mItems = itemListEntity.getItemList();
			mAdapter.updateData(mItems);
		}
	}
	
	private class RefreshTask extends AsyncTask<String, Integer, ItemListEntity>
	{
		@Override
		protected void onPostExecute(ItemListEntity result)
		{
			if(result == null)
			{
				itemLv.onRefreshComplete();
				Toast.makeText(ItemList.this, R.string.network_exception, Toast.LENGTH_SHORT).show();
				return;
			}
			ArrayList<FeedItem> newItems = new ArrayList<FeedItem>();
			File cache = FileUtils.UrlToFile(sectionUrl);
			SerializationHelper helper = SerializationHelper.newInstance();
			ArrayList<FeedItem> items = result.getItemList();
			ItemListEntity old = (ItemListEntity) helper.readObject(cache);
			String oldFirstDate = old.getFirstItem().getPubdate();
			int newCount = 0;
			for(FeedItem i : items)
			{
				if(i.getPubdate().equals(oldFirstDate))
				{
					itemLv.onRefreshComplete();
					Toast.makeText(ItemList.this, "暂无更新", Toast.LENGTH_SHORT).show();
					return;
				}
				newCount++;
				newItems.add(i);
			}
			helper.saveObject(result, cache);
			mAdapter.addItems(newItems);
			Toast.makeText(ItemList.this, "更新了" + newCount + "条", 
							Toast.LENGTH_SHORT).show();
			itemLv.onRefreshComplete();
		}

		@Override
		protected ItemListEntity doInBackground(String... params)
		{
			ItemListEntityParser parser = new ItemListEntityParser();
			return parser.parse(params[0]);
		}
		
	}
	
}
