package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.CategoryDetailAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.dao.FeedDao;
import com.dreamteam.app.entity.Feed;
import com.dreateam.app.ui.R;

/**
 * @description  
 * @author zcloud
 * @date 2013年11月14日
 */
public class FeedUI extends Activity
{
	public static final String tag = "CategoryDetail";
	
	private ListView detailList;
	private TextView titleTv;
	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	private CategoryDetailAdapter mAdapter;
	private FeedDao mDao;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData()
	{
		mDao = new FeedDao(this);
		Intent intent = getIntent();
		int cid = intent.getIntExtra("category", 1);
		Log.i(tag, "category.id = " + cid);
		String id = String.valueOf(cid);
		feeds = mDao.getListByCategoryId(id);
		//设置适配器
		mAdapter = new CategoryDetailAdapter(this, feeds, "feed");
		detailList.setAdapter(mAdapter);
	}

	private void initView()
	{
		setContentView(R.layout.category_detail);
		titleTv = (TextView) findViewById(R.id.cd_title_tv);
		findViewById(R.id.category_detail_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		detailList = (ListView) findViewById(R.id.catagory_detail_lv_feed);
		detailList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(!AppContext.isNetworkAvailable(FeedUI.this))
				{
					Toast.makeText(FeedUI.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
					return;
				}
				//feed预览
			}
		});
	}
}

