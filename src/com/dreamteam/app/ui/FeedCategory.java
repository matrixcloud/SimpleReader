package com.dreamteam.app.ui;

import com.dreamteam.app.adapter.FeedCategoryAdapter;
import com.dreateam.app.ui.R;

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

public class FeedCategory extends Activity
{

	private ListView categoryList;
	private ImageButton btn_add;
	
	private Intent intent = new Intent();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
	}

	private void initView()
	{
		setContentView(R.layout.feed_category);
		categoryList = (ListView) findViewById(R.id.feed_category_lsit);
		btn_add = (ImageButton) findViewById(R.id.feed_category_add_btn);
		btn_add.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Toast.makeText(FeedCategory.this, "开发中功能", Toast.LENGTH_SHORT).show();
			}
			
		});
		
		final FeedCategoryAdapter adapter = new FeedCategoryAdapter(this);
		categoryList.setAdapter(adapter);
		categoryList.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id)
			{
				intent.putExtra("category", position);
				intent.setClass(FeedCategory.this, CategoryDetail.class);
				FeedCategory.this.startActivity(intent);
			}
			
		});
		
	}
	
}
