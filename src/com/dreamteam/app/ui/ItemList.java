package com.dreamteam.app.ui;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.ItemListAdapter;
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
	
	private PullToRefreshListView itemList;
	private View footer;
	private ImageButton backBtn;
	private TextView feedTitleTv;
	private ItemListAdapter mAdapter;
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private Handler handler;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initHandler();
		initData();
		
	}

	private void initHandler()
	{
		handler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				switch(msg.what)
				{
				case 0:
					itemList.onRefreshComplete();
					Toast.makeText(ItemList.this, "暂无更新", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					footer.setVisibility(View.GONE);
					break;
				}
			}
		};
	}

	private void initView()
	{
		setContentView(R.layout.feed_item_list);
		
		backBtn = (ImageButton) findViewById(R.id.fil_back_btn);
		ButtonMonitor btnMonitor = new ButtonMonitor();
		backBtn.setOnClickListener(btnMonitor);
		
		feedTitleTv = (TextView) findViewById(R.id.fil_feed_title);
		itemList = (PullToRefreshListView) findViewById(R.id.fil_lv_feed_item);
		mAdapter = new ItemListAdapter(this);
		
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		footer = inflater.inflate(R.layout.list_footer, null);
		itemList.addFooterView(footer);
		itemList.setAdapter(mAdapter);
		
		itemList.setOnRefreshListener(new OnRefreshListener()
		{
			public void onRefresh()
			{
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							Thread.sleep(3000);
						}
						catch(InterruptedException e)
						{
							e.printStackTrace();
						}
						Message msg = handler.obtainMessage();
						msg.what = 0;
						handler.sendMessage(msg);
					}
				}.start();
			}
		});
		itemList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id)
			{
				Intent intent = new Intent();
				FeedItem item = items.get(position - 1);
				String title = item.getTitle();
				String contentEncoded = item.getContentEncoded();
				String pubdate = item.getPubdate();
				if(contentEncoded != null && contentEncoded.length() != 0)
				{
					intent.putExtra("item_detail", contentEncoded);
				}
				else
				{
					intent.putExtra("item_detail", items.get(position - 1).getDescription());
				}
				intent.putExtra("title", title);
				intent.putExtra("pubdate", pubdate);
				
				intent.setClass(ItemList.this, ItemDetail.class);
				ItemList.this.startActivity(intent);
			}
			
		});
		itemList.setOnScrollListener(new OnScrollListener()
		{
			private int lastItemIndex;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{

				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE 
							&& lastItemIndex == mAdapter.getCount())
				{
					footer.setVisibility(View.VISIBLE);
					new Thread()
					{
						@Override
						public void run()
						{
							try
							{
								sleep(3000);
							}
							catch(InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							Message msg = handler.obtainMessage();
							msg.what = 1;
							handler.sendMessage(msg);
						}
					}.start();
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount)
			{
				lastItemIndex = firstVisibleItem + visibleItemCount - 1 - 1;
			}
		});
	}

	private void initData()
	{
		intent = getIntent();
		String title = intent.getStringExtra("section_title") + "";
		feedTitleTv.setText(title);
		File file = FileUtils.getSectionCacheFile(
							intent.getStringExtra("url"));
		if(file.exists())
		{
			SerializationHelper seriaHelper = SerializationHelper.newInstance();
			ItemListEntity itemListEntity = (ItemListEntity) seriaHelper.readObject(file);
			items = itemListEntity.getItemList();
			mAdapter.updateData(items);
		}
	}

	private class ButtonMonitor implements OnClickListener
	{
		@Override
		public void onClick(View v)
		{
			switch(v.getId())
			{
			case R.id.fil_back_btn:
				finish();
				break;
			}
		}
		
	}
	
}
