package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.MPagerAdapter;
import com.dreamteam.app.db.DBHelper;
import com.dreamteam.custom.ui.PathAnimations;
import com.dreateam.app.ui.R;

public class Main extends FragmentActivity
{

	private ViewPager mPager;
	private MPagerAdapter mPagerAdapter;
	private RelativeLayout composerWrapper;
	private RelativeLayout composerShowHideBtn;
	private ImageView composerShowHideIconIv;
	private TextView pagerCounterTv;
	private BroadcastReceiver mReceiver;
	private ArrayList<MFragment> fragments = new ArrayList<MFragment>();
	private boolean areButtonsShowing;
	
	public static final int PAGE_SECTION_SIZE = 8;//一页8个section
	public static final String UPDATE_SECTION = "com.dreamteam.app.action.update_home_section";
	
	private int oldPageSize;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initPathMenu();
		try
		{
			initPager();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		initBroadcast();
	}

	private void initBroadcast()
	{
		mReceiver = new BroadcastReceiver()
		{
			
			@Override
			public void onReceive(Context context, Intent intent)
			{
				String action = intent.getAction();
				System.out.println("--------->>onReceive()");
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(UPDATE_SECTION);
		registerReceiver(mReceiver, filter);
	}

	private void initPathMenu()
	{
		composerWrapper = (RelativeLayout) findViewById(R.id.composer_wrapper);
		composerShowHideBtn = (RelativeLayout) findViewById(R.id.composer_show_hide_button);
		composerShowHideBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!areButtonsShowing)
				{
					PathAnimations.startAnimationsIn(composerWrapper, 300);
					composerShowHideIconIv
							.startAnimation(PathAnimations.getRotateAnimation(0,
									-270, 300));
				} else
				{
					PathAnimations
							.startAnimationsOut(composerWrapper, 300);
					composerShowHideIconIv
							.startAnimation(PathAnimations.getRotateAnimation(
									-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});
		composerShowHideIconIv = (ImageView) findViewById(R.id.composer_show_hide_button_icon);
		
		//Buttons事件处理
		for (int i = 0; i < composerWrapper.getChildCount(); i++)
		{
			composerWrapper.getChildAt(i).setOnClickListener(
			new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					switch(v.getId())
					{
					case R.id.composer_btn_user:
						Toast.makeText(Main.this, "Hello", Toast.LENGTH_SHORT).show();
						break;
					case R.id.composer_btn_setting:
						break;
					case R.id.composer_btn_feedback:
						break;
					case R.id.composer_btn_about:
						break;
					case R.id.composer_btn_add:
						openSubscribeCenter();
						break;
					case R.id.composer_btn_moon:
						break;
					}
				}
			});
		}
		
		composerShowHideBtn.startAnimation(PathAnimations
				.getRotateAnimation(0, 360, 200));
	}

	//打开订阅中心
	private void openSubscribeCenter()
	{
		Intent intent = new Intent();
		intent.setClass(this, FeedCategory.class);
		startActivity(intent);
	}
	
	private void initPager() throws Exception
	{
		//从数据库读数据
		DBHelper helper = new DBHelper(this, "reader.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("section", null, null, null, null, null, null);
		
		//pager分页
		int pageSize = 0;
		int sectionCount = cursor.getCount();
		db.close();
		if(sectionCount % PAGE_SECTION_SIZE == 0)
			pageSize = sectionCount / PAGE_SECTION_SIZE;
		else
			pageSize = sectionCount / PAGE_SECTION_SIZE + 1;
		for(int i = 0; i < pageSize; i++)
		{
			MFragment fragment = new MFragment();
			fragments.add(fragment);
		}
		mPagerAdapter = new MPagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(mPagerAdapter);
	}

	private void initView()
	{
		setContentView(R.layout.main);
		mPager = (ViewPager) findViewById(R.id.home_pager);
		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			
			@Override
			public void onPageSelected(int position)
			{
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
			{
				pagerCounterTv.setText((position + 1) + "/" + mPager.getChildCount());
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		
		pagerCounterTv = (TextView) findViewById(R.id.home_pager_counter);
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		//销毁广播接收器
		unregisterReceiver(mReceiver);
	}
	
}
