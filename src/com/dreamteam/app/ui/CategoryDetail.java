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
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.db.FeedDBHelper;
import com.dreamteam.app.entity.Feed;
import com.dreamteam.app.utils.CategoryNameExchange;
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
	private ArrayList<Feed> feeds = new ArrayList<Feed>();
	private CategoryDetailAdapter mAdapter;
	
	
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
		Intent intent = getIntent();
		String tableName = intent.getStringExtra("category");
		CategoryNameExchange exchange = new CategoryNameExchange(this);
		titleTv.setText(exchange.en2zh(tableName) + "");
		//读取数据库
		FeedDBHelper helper = new FeedDBHelper(this, FeedDBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(tableName, null, null, null, null, null, null);
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
		//设置适配器
		mAdapter = new CategoryDetailAdapter(this, feeds, tableName);
		detailList.setAdapter(mAdapter);
	}

	private void initView()
	{
		setContentView(R.layout.category_detail);
		detailList = (ListView) findViewById(R.id.catagory_detail_lv_feed);
		titleTv = (TextView) findViewById(R.id.cd_title_tv);
		detailList.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(!AppContext.isNetworkAvailable(CategoryDetail.this))
				{
					Toast.makeText(CategoryDetail.this, "请检查网络设置！", Toast.LENGTH_SHORT).show();
					return;
				}
				//feed预览
			}
		});
	}
}

