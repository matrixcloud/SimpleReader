package com.dreamteam.app.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dreamteam.app.adapter.GuideViewPagerAdapter;
import com.dreamteam.app.commons.AppConfig;
import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.db.FeedDBManager;
import com.dreateam.app.ui.R;

/**
 * 
 * @{# GuideActivity.java Create on 2013-5-2 下午10:59:08
 * 
 *     class desc: 引导界面，初始化软件数据
 * 
 *     <p>
 *     Copyright: Copyright(c) 2013
 *     </p>
 * @Version 1.0
 * @Author <a href="mailto:gaolei_xj@163.com">Leo</a>
 * 
 * 
 */
public class GuideActivity extends Activity implements OnPageChangeListener
{
	private ViewPager vp;
	private GuideViewPagerAdapter vpAdapter;
	private List<View> views;

	// 底部小点图片
	private ImageView[] dots;

	// 记录当前选中位置
	private int currentIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide);

		// 初始化页面
		initViews();

		// 初始化底部小点
		initDots();
		writeDB();
		//写入计算缓存天数的文件
		writeSaveDaysFile();
		//删除之前的缓存文件
		AppContext.clearSdCache();
	}

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

	private void initViews()
	{
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.what_new_one, null));
		views.add(inflater.inflate(R.layout.what_new_two, null));
		views.add(inflater.inflate(R.layout.what_new_three, null));
		views.add(inflater.inflate(R.layout.what_new_four, null));

		// 初始化Adapter
		vpAdapter = new GuideViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
	}

	private void initDots()
	{
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++)
		{
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	private void setCurrentDot(int position)
	{
		if (position < 0 || position > views.size() - 1
				|| currentIndex == position)
		{
			return;
		}

		dots[position].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = position;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0)
	{
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2)
	{
	}

	// 当新的页面被选中时调用
	@Override
	public void onPageSelected(int arg0)
	{
		// 设置底部小点选中状态
		setCurrentDot(arg0);
	}

}
