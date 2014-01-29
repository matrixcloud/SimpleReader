package com.dreamteam.app.ui;

import java.util.ArrayList;
import java.util.HashSet;

import javax.net.ssl.ManagerFactoryParameters;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.MPagerAdapter;
import com.dreamteam.app.adapter.SectionGridAdapter;
import com.dreamteam.app.db.DBHelper;
import com.dreamteam.app.entity.Section;
import com.dreamteam.custom.ui.PathAnimations;
import com.dreateam.app.ui.R;

public class Main extends FragmentActivity
{
	public static final String tag = "Main";

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
	public static final String ADD_SECTION = "com.dreamteam.app.action.add_section";
	public static final String DELETE_SECTION = "com.dreamteam.app.action.delete_section";
	public static final int PAGE_SIZE_INCREASE = 1;
	public static final int PAGE_SIZE_NOT_CHANGE = 0;
	public static final int PAGE_SIZE_DECREASE = -1;
	
	
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
				if(action.equals(ADD_SECTION))
				{
					MFragment lastFragment = mPagerAdapter.getLastFragment();
					//最后一个已满或pagerAdapter为空，添加mFragment
					if(lastFragment == null || lastFragment.isFull())
					{
						MFragment fragment = new MFragment();
						mPagerAdapter.addItem(fragment);
						mPagerAdapter.notifyDataSetChanged();
						fragment.onCreateView(getLayoutInflater(), mPager, null);
						lastFragment = fragment;
					}
					else
					{
						SectionGridAdapter gridAdapter = lastFragment.getGridAdapter();
						gridAdapter.addItem(getNewSection());
					}
				}
				else if(action.equals(DELETE_SECTION))
				{
					MFragment decreaseFragment = null;
					
					String url = intent.getStringExtra("url");
					for(int i = 0; i<mPagerAdapter.getCount(); i++)
					{
						MFragment fragment = (MFragment) mPagerAdapter.getFragment(i);
						if(fragment.getGridAdapter().removeItem(url))
						{
							decreaseFragment = fragment;
						}
					}
					//重新排列
					MFragment lastFragment = mPagerAdapter.getLastFragment();
					if(!lastFragment.equals(decreaseFragment))
					{
						SectionGridAdapter lastAdapter = lastFragment.getGridAdapter();
						Section section = lastAdapter.getLastItem();
						decreaseFragment.getGridAdapter().addItem(section);
						lastAdapter.removeItem(section.getUrl());
					}
					if(!mPagerAdapter.isOneLesser() && lastFragment.getGridAdapter().isEmpty())
						mPagerAdapter.removeLastItem();
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ADD_SECTION);
		filter.addAction(DELETE_SECTION);
		registerReceiver(mReceiver, filter);
	}

	//获取表新加入的section
	private Section getNewSection()
	{
		Section section = new Section();
		DBHelper helper = new DBHelper(Main.this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.SECTION_TABLE_NAME, null, null, null, null, null, null);
		if(cursor.moveToLast())
		{
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String tableName = cursor.getString(cursor.getColumnIndex("table_name"));
			section.setTitle(title);
			section.setUrl(url);
			section.setTableName(tableName);
		}
		db.close();
		return section;
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
		DBHelper helper = new DBHelper(this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.SECTION_TABLE_NAME, null, null, null, null, null, null);
		
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
		//保证有一个fragment
		if(fragments.isEmpty())
			fragments.add(new MFragment());
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
