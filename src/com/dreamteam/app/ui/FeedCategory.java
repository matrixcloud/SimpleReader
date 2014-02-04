package com.dreamteam.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.adapter.FeedCategoryAdapter;
import com.dreateam.app.ui.R;

public class FeedCategory extends Activity
{

	private ListView categoryList;
	private ImageButton btn_add;
	private String[] categories;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView()
	{
		//初始分类名称
		categories = getResources().getStringArray(R.array.feed_category_en);
		
		setContentView(R.layout.feed_category);
		categoryList = (ListView) findViewById(R.id.feed_category_lsit);
		btn_add = (ImageButton) findViewById(R.id.feed_category_add_btn);
		btn_add.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Toast.makeText(FeedCategory.this, "开发中功能", Toast.LENGTH_SHORT)
						.show();
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
		
		final FeedCategoryAdapter adapter = new FeedCategoryAdapter(this);
		categoryList.setAdapter(adapter);
		categoryList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				Intent intent = new Intent();
				intent.putExtra("category", categories[position]);
				intent.setClass(FeedCategory.this, CategoryDetail.class);
				FeedCategory.this.startActivity(intent);
			}
		});
	}
}
