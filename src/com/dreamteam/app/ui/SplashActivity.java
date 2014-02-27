package com.dreamteam.app.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dreamteam.app.commons.AppConfig;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FeedDBManager;
import com.dreateam.app.ui.R;

/**
 * 
 * @{# SplashActivity.java Create on 2013-5-2 下午9:10:01
 * 
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 * 
 * 
 */
public class SplashActivity extends Activity
{
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 2000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

	/**
	 * Handler:跳转到不同界面
	 */
	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
//				goGuide();
				writeDB();
				initSection();
				writeSaveDaysFile();
				goHome();
				break;
			}
			super.handleMessage(msg);
		}
	};


	private void writeSaveDaysFile()
	{
		String fileName = getFilesDir().getAbsolutePath() + File.separator 
				+ AppConfig.PREF_DEPRECATED;
		File file = new File(fileName);
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent != null)
		{
			String title =intent.getStringExtra("section_title");
			if(title != null)
			{
				intent.setClass(this, ItemList.class);
				startActivity(intent);
			}
		}
		setContentView(R.layout.splash_acitvity);
		init();
	}

	private void init()
	{
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn)
		{
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
			mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
		}
		else
		{
			Editor editor = preferences.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}

	}

	private void goHome()
	{
		Intent intent = new Intent(SplashActivity.this, Main.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide()
	{
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
	
	/**
	 * 土办法
	 */
	private void initSection()
	{
		DbManager mgr = new DbManager(this, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		SectionHelper.insert(db, "news", "国内新闻", "http://n.rss.qq.com/rss/qqmail_rss.php?id=2");
		SectionHelper.insert(db, "news", "国际时事", "http://n.rss.qq.com/rss/qqmail_rss.php?id=3");
		SectionHelper.insert(db, "science", "科技松鼠会", "http://songshuhui.net/feed");
		SectionHelper.insert(db, "science", "36氪", "http://www.36kr.com/feed");
		db.close();
	}
	
	private void writeDB()
	{
		InputStream inputStream = null;
		try
		{
			inputStream = getAssets().open("feed.db");
			FeedDBManager helper = new FeedDBManager(this, FeedDBManager.DB_NAME, null, 1);
			SQLiteDatabase db = helper.getWritableDatabase();
			File dbFile = new File(db.getPath());
			if(dbFile.exists())
			{
				dbFile.delete();
			}
			FileOutputStream fos = null;
			
			try
			{
				fos = new FileOutputStream(dbFile);
				byte buffer[] = new byte[1024 * 4];
				while((inputStream.read(buffer)) != -1)
				{
					fos.write(buffer);
				}
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					if(inputStream != null)
					{
						inputStream.close();
						inputStream = null;
					}
					if(fos != null)
					{
						fos.close();
						fos = null;
					}
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				db.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}	
}
