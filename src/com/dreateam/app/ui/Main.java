package com.dreateam.app.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreateam.app.adpater.MPagerAdapter;
import com.dreateam.custom.ui.PathAnimations;

public class Main extends FragmentActivity
{
	private ViewPager mPager;
	private RelativeLayout composerWrapper;
	private RelativeLayout composerShowHideBtn;
	private ImageView composerShowHideIconIv;
	private TextView pagerCounterTv;
	private ArrayList<MFragment> fragments = new ArrayList<MFragment>();
	private boolean areButtonsShowing;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();
		initPathMenu();
		initPager();
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

	private void initPager()
	{
		MFragment fragment = new MFragment();
		MFragment fragment1 = new MFragment();
		fragments.add(fragment);
		fragments.add(fragment1);
		
		MPagerAdapter mPagerAdapter = new MPagerAdapter(getSupportFragmentManager(), fragments);
		mPager.setAdapter(mPagerAdapter);
	}


	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
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
	
}
