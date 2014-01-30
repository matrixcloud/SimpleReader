package com.dreamteam.app.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.app.adapter.GridAdapter;
import com.dreamteam.app.adapter.MPagerAdapter;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.ItemListEntityParser;
import com.dreamteam.app.commons.SerializationHelper;
import com.dreamteam.app.db.DBHelper;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.app.entity.Section;
import com.dreamteam.app.utils.FileUtils;
import com.dreamteam.app.utils.ImageUtils;
import com.dreamteam.custom.ui.PathAnimations;
import com.dreateam.app.ui.R;
import com.umeng.socialize.controller.UMInfoAgent;

public class Main extends FragmentActivity
{
	public static final String tag = "Main";

	private ViewPager mPager;
	private MPagerAdapter mPagerAdapter;
	private RelativeLayout composerWrapper;
	private RelativeLayout composerShowHideBtn;
	private ImageView composerShowHideIconIv;
	private TextView pageTv;
	private RelativeLayout homeLoadingLayout;
	private ArrayList<GridView> gridViews = new ArrayList<GridView>();
	private ArrayList<GridAdapter> gridAdapters = new ArrayList<GridAdapter>();
	private BroadcastReceiver mReceiver;
	private boolean areButtonsShowing;
	public static final int PAGE_SECTION_SIZE = 8;// 一页8个section
	public static final String ADD_SECTION = "com.dreamteam.app.action.add_section";
	public static final String DELETE_SECTION = "com.dreamteam.app.action.delete_section";
	public static final int PAGE_SIZE_INCREASE = 1;
	public static final int PAGE_SIZE_NOT_CHANGE = 0;
	public static final int PAGE_SIZE_DECREASE = -1;
	private Intent mIntent;
	private boolean exit = false;//双击退出
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initPathMenu();
		initPager();
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
				if (action.equals(ADD_SECTION))
				{
					// 最后一个adapter为空或已满，新生一个gridView
					Log.d(tag, gridAdapters.size() + "adapters");
					Log.d(tag, gridViews.size() + "views");
					GridAdapter lastGridAdapter = getLastGridAdapter();
					if (lastGridAdapter == null || lastGridAdapter.isFull())
					{
						Log.i(tag, "here");
						addGridView();
					} else
					{
						// 最后一个gridAdapter添加section
						lastGridAdapter.addItem(getNewSection());
					}
				} else if (action.equals(DELETE_SECTION))
				{
					// 根据移除此section
					GridAdapter deCreaseAdapter = null;

					String url = intent.getStringExtra("url");
					for (int i = 0; i < gridAdapters.size(); i++)
					{
						deCreaseAdapter = gridAdapters.get(i);
						if (deCreaseAdapter.removeItem(url))
						{
							break;
						}
					}
					GridAdapter lastAdapter = getLastGridAdapter();
					if (lastAdapter.isEmpty())
					{
						if (gridViews.size() <= 1)
						{
							return;
						}
						removeLastGridView();
						return;
					}
					if (!lastAdapter.equals(deCreaseAdapter))
					{
						Section section = lastAdapter.getLastItem();
						deCreaseAdapter.addItem(section);
						lastAdapter.removeItem(section.getUrl());
					}
					if (lastAdapter.isEmpty())
					{
						if (gridViews.size() <= 1)
						{
							return;
						}
						removeLastGridView();
						removeLastGridAdapter();
					}
				}
			}
		};
		IntentFilter filter = new IntentFilter();
		filter.addAction(ADD_SECTION);
		filter.addAction(DELETE_SECTION);
		registerReceiver(mReceiver, filter);
	}

	// 获取表新加入的section
	private Section getNewSection()
	{
		Section section = new Section();
		DBHelper helper = new DBHelper(Main.this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.SECTION_TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToLast())
		{
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String tableName = cursor.getString(cursor
					.getColumnIndex("table_name"));
			section.setTitle(title);
			section.setUrl(url);
			section.setTableName(tableName);
		}
		db.close();
		return section;
	}

	private void initPathMenu()
	{
		PathAnimations.initOffset(this);
		composerWrapper = (RelativeLayout) findViewById(R.id.composer_wrapper);
		composerShowHideBtn = (RelativeLayout) findViewById(R.id.composer_show_hide_button);
		composerShowHideIconIv = (ImageView) findViewById(R.id.composer_show_hide_button_icon);
		composerShowHideBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if (!areButtonsShowing)
				{
					PathAnimations.startAnimationsIn(composerWrapper, 300);
					composerShowHideIconIv.startAnimation(PathAnimations
							.getRotateAnimation(0, -270, 300));
				} else
				{
					PathAnimations.startAnimationsOut(composerWrapper, 300);
					composerShowHideIconIv.startAnimation(PathAnimations
							.getRotateAnimation(-270, 0, 300));
				}
				areButtonsShowing = !areButtonsShowing;
			}
		});

		// Buttons事件处理
		for (int i = 0; i < composerWrapper.getChildCount(); i++)
		{
			composerWrapper.getChildAt(i).setOnClickListener(
					new View.OnClickListener()
					{
						@Override
						public void onClick(View v)
						{
							switch (v.getId())
							{
							case R.id.composer_btn_user:
								login();
								break;
							case R.id.composer_btn_setting:
								openSetting();
								break;
							case R.id.composer_btn_feedback:
								feedback();
								break;
							case R.id.composer_btn_about:
								swithBg();
								break;
							case R.id.composer_btn_add:
								openSubscribeCenter();
								break;
							case R.id.composer_btn_moon:
								break;
							}
							overridePendingTransition(
									R.anim.anim_fromright_toup6,
									R.anim.anim_down_toleft6);
						}

					});
		}

		composerShowHideBtn.startAnimation(PathAnimations.getRotateAnimation(0,
				360, 200));
	}

	//切换壁纸
	private void swithBg()
	{
		Intent intent = new Intent();
		intent.setClass(Main.this, SwitchBg.class);
		Main.this.startActivity(intent);
	}
	
	//反馈
	private void feedback()
	{
		Intent intent = new Intent();
		intent.setClass(Main.this, FeedbackUI.class);
		Main.this.startActivity(intent);
	}
	
	//打开设置界面
	private void openSetting()
	{
		Intent intent = new Intent();
		intent.setClass(Main.this, Setting.class);
		Main.this.startActivity(intent);
	}
	
	//登陆
	private void login()
	{
		if(UMInfoAgent.isLogin(this))
		{
			Toast.makeText(this, "你已登陆！", Toast.LENGTH_SHORT).show();
			return;
		}
		new LoginDialog().show(getSupportFragmentManager(), tag);
	}
	
	// 打开订阅中心
	private void openSubscribeCenter()
	{
		Intent intent = new Intent();
		intent.setClass(this, FeedCategory.class);
		startActivity(intent);
	}

	private void initPager()
	{
		int pageSize = getPageSize();
		for (int i = 0; i < pageSize; i++)
		{
			gridViews.add(newGridView(i));
			mPagerAdapter.notifyDataSetChanged();
		}
	}

	private void initView()
	{
		setContentView(R.layout.main);
		pageTv = (TextView) findViewById(R.id.home_page_tv);
		homeLoadingLayout = (RelativeLayout) findViewById(R.id.home_loading_layout);
		mPager = (ViewPager) findViewById(R.id.home_pager);
		mPager.setOnPageChangeListener(new OnPageChangeListener()
		{
			@Override
			public void onPageSelected(int position)
			{
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels)
			{
				pageTv.setText(position + 1 + "");
			}

			@Override
			public void onPageScrollStateChanged(int state)
			{
			}
		});
		mPagerAdapter = new MPagerAdapter(gridViews);
		mPager.setAdapter(mPagerAdapter);
	}

	private GridView newGridView(int currentPage)
	{
		GridView grid = new GridView(this);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		grid.setLayoutParams(params);
		int right = ImageUtils.dip2px(this, 50);
		int left = ImageUtils.dip2px(this, 20);
		int top = ImageUtils.dip2px(this, 20);
		int bottom = ImageUtils.dip2px(this, 20);
		grid.setPadding(left, top, right, bottom);
		grid.setNumColumns(2);
		grid.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id)
			{
				GridAdapter adapter = gridAdapters.get(
							mPager.getCurrentItem());
				Section section = (Section) adapter.getItem(position);
				String title = section.getTitle();
				String url = section.getUrl();
				//初始intent
				mIntent = new Intent();
				mIntent.putExtra("section_title", title);
				mIntent.putExtra("url", url);
				mIntent.setClass(Main.this, ItemList.class);
				
				
				//读取缓存
				File cache = FileUtils.getSectionCacheFile(url);
				if(cache.exists())
				{
					
					Main.this.startActivity(mIntent);
				}
				else
				{
					if(!AppContext.isNetworkAvailable(Main.this))
					{
						Toast.makeText(Main.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
						return;
					}
					//异步加载数据
					Log.d(tag, "" + url);
					new LoadDataTask().execute(url);
				}
			}
		});
		
		ArrayList<Section> sections = null;
		try
		{
			sections = readSections(currentPage);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		GridAdapter gridAdapter = new GridAdapter(this, sections);
		gridAdapters.add(gridAdapter);
		grid.setAdapter(gridAdapter);
		return grid;
	}

	private ArrayList<Section> readSections(int page) throws Exception
	{
		ArrayList<Section> sections = null;
		int len = 0;// 表长
		int start = 0;// 其实读
		int end = 0;// 结尾
		Log.i(tag, "page = " + page);
		// 从数据库读数据
		DBHelper helper = new DBHelper(this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.SECTION_TABLE_NAME, null, null, null,
				null, null, null);
		len = cursor.getCount();
		start = page * Main.PAGE_SECTION_SIZE;
		if (cursor.moveToPosition(start))
		{
			sections = new ArrayList<Section>();

			int offset = start + Main.PAGE_SECTION_SIZE;
			end = len < offset ? len : offset;
			for (int i = start; i < end; i++)
			{
				Section s = new Section();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				s.setTitle(title);
				s.setUrl(url);
				sections.add(s);
				cursor.moveToNext();
			}
		}
		db.close();
		return sections;
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		// 销毁广播接收器
		unregisterReceiver(mReceiver);
	}

	private void addGridView()
	{
		int lastPage = getPageSize() - 1;
		GridView grid = newGridView(lastPage);
		gridViews.add(grid);
		mPagerAdapter.notifyDataSetChanged();
	}

	private void removeLastGridView()
	{
		if (gridViews.isEmpty())
			return;
		gridViews.remove(gridViews.size() - 1);
		mPagerAdapter.notifyDataSetChanged();
	}

	private GridAdapter getLastGridAdapter()
	{
		if (gridAdapters.isEmpty())
			return null;
		return gridAdapters.get(gridAdapters.size() - 1);
	}

	private void removeLastGridAdapter()
	{
		if (gridAdapters.isEmpty())
			return;
		gridAdapters.remove(gridAdapters.size() - 1);
	}

	// 从1记
	private int getPageSize()
	{
		// 从数据库读数据
		DBHelper helper = new DBHelper(this, DBHelper.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query(DBHelper.SECTION_TABLE_NAME, null, null, null,
				null, null, null);

		// pager分页
		int pageSize = 0;
		int sectionCount = cursor.getCount();
		db.close();
		if (sectionCount % PAGE_SECTION_SIZE == 0)
			pageSize = sectionCount / PAGE_SECTION_SIZE;
		else
			pageSize = sectionCount / PAGE_SECTION_SIZE + 1;
		return pageSize;
	}

	
	private class LoadDataTask extends AsyncTask<String, Integer, ItemListEntity>
	{

		@Override
		protected void onPreExecute()
		{
			homeLoadingLayout.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ItemListEntity result)
		{
			homeLoadingLayout.setVisibility(View.GONE);
			//跳转界面
			if(result != null && mIntent != null)
			{
				Main.this.startActivity(mIntent);
			}
			else
			{
				Toast.makeText(Main.this, "网络异常！", Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected ItemListEntity doInBackground(String... params)
		{
			ItemListEntityParser parser = new ItemListEntityParser();
			ItemListEntity entity = parser.parse(params[0]);
			if(entity != null)
			{
				SerializationHelper helper = SerializationHelper.newInstance();
				File file = FileUtils.createSectionCacheFile(params[0]);
				helper.saveObject(entity, file);
			}
			return entity;
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(exit)
		{
			finish();
			return true;
		}
		else
		{
			if(mIntent != null)
			{
				mIntent = null;
				return false;
			}
			Toast.makeText(Main.this, "再按下返回退出程序", Toast.LENGTH_SHORT).show();
			exit = true;
			return false;
		}
	}
}