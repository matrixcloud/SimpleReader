package com.dreamteam.app.ui;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.ViewGroup;
import android.webkit.CacheManager;
import android.widget.ListView;
import android.widget.Toast;

import com.dreamteam.app.utils.FileUtils;
import com.dreateam.app.ui.R;

@SuppressWarnings("deprecation")
public class Setting extends PreferenceActivity
{
	private SharedPreferences mPreferences;
	private CheckBoxPreference imageLoadCb;
	private Preference clearCachePref;
	
	
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initPreference();
	}

	private void initView()
	{
		addPreferencesFromResource(R.xml.preference);
		ListView mLv = getListView();
		mLv.setBackgroundColor(0);
		mLv.setCacheColorHint(0);
		((ViewGroup) mLv.getParent()).removeView(mLv);
		ViewGroup localViewGroup = (ViewGroup) getLayoutInflater().inflate(
				R.layout.setting, null);
		((ViewGroup) localViewGroup.findViewById(R.id.setting_content))
				.addView(mLv, -1, -1);
		setContentView(localViewGroup);
	}
	
	private void initPreference()
	{
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		imageLoadCb = (CheckBoxPreference) findPreference("pref_imageLoad");
		imageLoadCb.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				if(mPreferences.getBoolean("imageLoad", true))
				{
					//显示图片
					imageLoadCb.setSummary("加载图片（WIFI默认加载图片）");
				}
				else
				{
					imageLoadCb.setSummary("不加载图片（WIFI默认加载图片）");
				}
				return false;
			}
		});
		//缓存
		// 计算缓存大小
		long fileSize = 0;
		String cacheSize = "0KB";
		File cacheFile = getCacheDir();
		
		fileSize = FileUtils.getDirSize(cacheFile);
		if(fileSize > 0)
			cacheSize = FileUtils.formatFileSize(fileSize);
		
		clearCachePref = findPreference("pref_clearCache");
		clearCachePref.setSummary(cacheSize);
		clearCachePref.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				new AsyncTask<Integer, Integer, Integer>()
				{

					@Override
					protected void onPostExecute(Integer result)
					{
						Toast.makeText(Setting.this, "清理完毕！", Toast.LENGTH_SHORT).show();
						clearCachePref.setSummary("0KB");
					}

					@Override
					protected Integer doInBackground(Integer... params)
					{
						clearWebViewCache();
						return 0;
					}

				};
				return false;
			}

		});
	}
	
	//清除webview缓存
	public void clearWebViewCache()
	{
		File file = CacheManager.getCacheFileBaseDir();  
		if (file != null && file.exists() && file.isDirectory()) {  
		    for (File item : file.listFiles()) {  
		    	item.delete();  
		    }  
		    file.delete();  
		}  		  
		deleteDatabase("webview.db");  
		deleteDatabase("webview.db-shm");  
		deleteDatabase("webview.db-wal");  
		deleteDatabase("webviewCache.db");  
		deleteDatabase("webviewCache.db-shm");  
		deleteDatabase("webviewCache.db-wal");  
	}	
}
