package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.CategoryDetailAdapter;
import com.dreamteam.app.commons.Appcontext;
import com.dreamteam.app.db.FeedDBHelper;
import com.dreamteam.app.entity.Feed;
import com.dreateam.app.ui.R;

/**
 * @description TODO
 * @author zcloud
 * @date 2013年11月14日
 */
public class CategoryDetail extends Activity
{
	public static final String tag = "CategoryDetail";
	
	private ListView detailList;
	private TextView titleTv;
	private String[] categories;

	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	
	private CategoryDetailAdapter adapter;
	
	private static final int POS_NEWS = 0;
	private static final int POS_SCIENCE = 1;
	private static final int POS_CULTURE = 2;
	private static final int POS_SPORTS = 3;
	private static final int POS_MANGA = 4;
	private static final int POS_BLOG = 5;
	private static final int POS_ENGLISH = 6;
	private static final int POS_FUN = 7;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		try
		{
			initData();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void initData() throws Exception
	{
		categories = getResources()
				.getStringArray(R.array.feed_category);
		
		Intent intent = getIntent();
		//读取数据库
		FeedDBHelper helper = new FeedDBHelper(this, "feed.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = null;
		int key = intent.getIntExtra("category", -1);
		switch(key)
		{
		case POS_NEWS:
			adapter.setTabelName("news");
			cursor = db.query("news", null, null, null, null, null, null);
			break;
		case POS_SCIENCE:
			adapter.setTabelName("science");
			cursor = db.query("science", null, null, null, null, null, null);
			break;
		case POS_CULTURE:
			adapter.setTabelName("culture");
			cursor = db.query("culture", null, null, null, null, null, null);
			break;
		case POS_SPORTS:
			adapter.setTabelName("sports");
			cursor = db.query("sports", null, null, null, null, null, null);
			break;
		case POS_MANGA:
			adapter.setTabelName("manga");
			cursor = db.query("manga", null, null, null, null, null, null);
			break;
		case POS_BLOG:
			adapter.setTabelName("blog");
			cursor = db.query("blog", null, null, null, null, null, null);
			break;
		case POS_ENGLISH:
			adapter.setTabelName("english");
			cursor = db.query("english", null, null, null, null, null, null);
			break;
		case POS_FUN:
			adapter.setTabelName("fun");
			cursor = db.query("fun", null, null, null, null, null, null);
			break;
		}
		//设置title
		titleTv.setText(categories[key]);
		if (cursor.moveToFirst())
		{
			for (int i = 0, n = cursor.getCount(); i < n; i++)
			{
				Feed f = new Feed();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				int selectStatus = cursor.getInt(cursor
						.getColumnIndex("select_status"));
				f.setTitle(title);
				f.setUrl(url);
				f.setSelectStatus(selectStatus);
				feeds.add(f);
				cursor.moveToNext();
			}
		}
		db.close();
		adapter.updateData(feeds);
	}

	private void initView()
	{
		setContentView(R.layout.category_detail);
		detailList = (ListView) findViewById(R.id.catagory_detail_lv_feed);
		titleTv = (TextView) findViewById(R.id.cd_title_tv);
		adapter = new CategoryDetailAdapter(this, feeds);
		detailList.setAdapter(adapter);
		
		detailList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(!Appcontext.isNetworkAvailable(CategoryDetail.this))
				{
					Toast.makeText(CategoryDetail.this, "请检查网络设置！", Toast.LENGTH_SHORT).show();
					return;
				}
				//feed预览
			}
		});
	}
}

