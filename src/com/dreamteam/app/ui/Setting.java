package com.dreamteam.app.ui;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.dreateam.app.ui.R;

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
					imageLoadCb.setSummary("������ͼƬ��Ĭ��WIFI���أ�");
				}
				else
				{
					imageLoadCb.setSummary("����ͼƬ��Ĭ��WIFI���أ�");
				}
				return false;
			}
		});
		//缓存
		final File cache = getCacheDir();
		clearCachePref = findPreference("pref_clearCache");
		clearCachePref.setSummary("0KB");
		clearCachePref.setOnPreferenceClickListener(new OnPreferenceClickListener()
		{
			@Override
			public boolean onPreferenceClick(Preference preference)
			{
				clearCache(cache);
				Toast.makeText(Setting.this, "清理完毕！", Toast.LENGTH_SHORT).show();
				return false;
			}

		});
	}
	
	private void clearCache(File cache)
	{
		cache.delete();
	}
}
