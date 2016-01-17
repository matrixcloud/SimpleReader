package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.adapter.FeedCategoryAdapter;
import com.dreamteam.app.dao.FeedCategoryDao;
import com.dreamteam.app.entity.FeedCategory;
import com.dreateam.app.ui.R;

public class FeedCategoryUI extends FragmentActivity
{
	public static final String tag = "FeedCategoryUI";
	private ListView categoryLv;
	private ImageButton addImgBtn;
	private ArrayList<FeedCategory> fcList = new ArrayList<FeedCategory>();
	private FeedCategoryAdapter mAdapter;
	private FeedCategoryDao fcDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView()
	{
		setContentView(R.layout.feed_category);
		categoryLv = (ListView) findViewById(R.id.feed_category_lsit);
		addImgBtn = (ImageButton) findViewById(R.id.feed_category_add_btn);
		addImgBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
//				if(!AppContext.isNetworkAvailable(FeedCategory.this))
//				{
//					Toast.makeText(FeedCategory.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				new AddDialog().show(getSupportFragmentManager(), "添加Feed");
				Toast.makeText(FeedCategoryUI.this, "开发中功能", Toast.LENGTH_SHORT).show();
			}
		});
		findViewById(R.id.feed_category_btn_back).setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		fcDao = new FeedCategoryDao(this);
		fcList = fcDao.getList();
		mAdapter = new FeedCategoryAdapter(this, fcList);
		categoryLv.setAdapter(mAdapter);
		categoryLv.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent();
				intent.putExtra("category", fcList.get(position).getId());
				intent.setClass(FeedCategoryUI.this, FeedUI.class);
				FeedCategoryUI.this.startActivity(intent);
			}
		});
	}
}
