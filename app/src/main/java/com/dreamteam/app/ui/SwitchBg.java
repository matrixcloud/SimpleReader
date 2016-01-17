package com.dreamteam.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.dreamteam.app.commons.AppContext;
import com.dreateam.app.ui.R;

public class SwitchBg extends Activity implements
		AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory
{
	
    private Integer[] mImageIds = {
    		R.drawable.home_bg_night,
    		R.drawable.home_bg_default,
    		R.drawable.home_bg_0
    };
    
    private ImageButton returnBtn;
    private ImageButton okBtn;
    private int selectedPosition;
	public static final String SWITCH_HOME_BG = "com.dreamteam.action.swtich_home_bg";
    
    
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.switch_bg);
		
		mSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		mSwitcher.setFactory(this);
		mSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_in));
		mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.fade_out));

		Gallery g = (Gallery) findViewById(R.id.gallery);
		g.setAdapter(new ImageAdapter(this));
		g.setOnItemSelectedListener(this);
		
		returnBtn = (ImageButton) findViewById(R.id.switch_bg_back);
		returnBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				finish();
			}
		});
		okBtn = (ImageButton) findViewById(R.id.switch_bg_ok);
		okBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//改变配置,发送广播
				SharedPreferences prefs = AppContext.getPrefrences(SwitchBg.this);
				Editor editor = prefs.edit();
				editor.putInt("home_bg", mImageIds[selectedPosition]);
				editor.commit();
				Intent intent = new Intent();
				intent.setAction(SWITCH_HOME_BG);
				intent.putExtra("home_bg_id", mImageIds[selectedPosition]);
				sendBroadcast(intent);
				Toast.makeText(SwitchBg.this, R.string.bg_switch_success, Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id)
	{
		selectedPosition = position;
		mSwitcher.setImageResource(mImageIds[position]);
	}

	public void onNothingSelected(AdapterView<?> parent)
	{
	}

	public View makeView()
	{
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_CENTER);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		return i;
	}

	private ImageSwitcher mSwitcher;

	public class ImageAdapter extends BaseAdapter
	{
		public ImageAdapter(Context c)
		{
			mContext = c;
		}

		public int getCount()
		{
			return mImageIds.length;
		}

		public Object getItem(int position)
		{
			return position;
		}

		public long getItemId(int position)
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent)
		{
			ImageView i = new ImageView(mContext);

			i.setImageResource(mImageIds[position]);
			i.setAdjustViewBounds(true);
			i.setLayoutParams(new Gallery.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			i.setBackgroundResource(R.drawable.picture_frame);
			return i;
		}

		private Context mContext;

	}
}
