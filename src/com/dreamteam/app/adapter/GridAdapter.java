package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.app.commons.AppContext;
import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.db.FeedDBManager;
import com.dreamteam.app.entity.Section;
import com.dreamteam.app.ui.Main;
import com.dreamteam.app.utils.FileUtils;
import com.dreateam.app.ui.R;

public class GridAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Section> sections;
	//deleteButton是否可见
	private int isVisible = 0;//deleteButton是否可见
	private int[] visibleStates = {View.GONE,View.VISIBLE};
	public static final String ACTION_INSTALL_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
	public static final String ACTION_ENTER_BY_SHORTCUT = "com.dreateam.action.ENTER_BY_SHORTCUT";
	
	public GridAdapter(Context context, ArrayList<Section> sections)
	{
		this.context = context;
		this.sections = sections;
	}
	
	
	@Override
	public int getCount()
	{
		return sections.size();
	}

	@Override
	public Object getItem(int postion)
	{
		return sections.get(postion);
	}

	@Override
	public long getItemId(int id)
	{
		return id;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		
		if(convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.section_item, null);
			holder = new ViewHolder();
			holder.itemTitle = (TextView) convertView.findViewById(R.id.item_text);
			holder.delteBtn = (ImageView) convertView.findViewById(R.id.item_btn_delete);
			holder.addLauncherBtn = (ImageView) convertView.findViewById(R.id.item_btn_add_launcher);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		
		final Section section = sections.get(position);
		if(section != null)
		{
			holder.itemTitle.setText(section.getTitle());
		}
		holder.delteBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				final String url = section.getUrl();
				final String tableName = section.getTableName();
				//删除当前section
				Intent intent = new Intent();
				intent.setAction(Main.ACTION_DELETE_SECTION);
				intent.putExtra("url", url);
				context.sendBroadcast(intent);
				new Thread()
				{
					public void run()
					{
						//移除数据库中的记录
						DbManager mgr = new DbManager(context,
								DbManager.DB_NAME, null, 1);
						SectionHelper.removeRecord(mgr.getWritableDatabase(),
								url);
						//修改数据库feed.db的状态
						new FeedDBManager(context, FeedDBManager.DB_NAME, null,
								1).updateState(tableName, 0, url);
						//移除缓存
						FileUtils.deleteDirectory(AppContext.getSectionCache(
								url).getAbsolutePath());
					}
				}.start();
			}
		});
		holder.delteBtn.setVisibility(visibleStates[isVisible]);
		holder.addLauncherBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				addShortcut(section.getTitle(), section.getUrl());
			}
		});
		holder.addLauncherBtn.setVisibility(visibleStates[isVisible]);
		return convertView;
	}
	
	private static final class ViewHolder
	{
		TextView itemTitle;
		ImageView delteBtn;
		ImageView addLauncherBtn;
	}
	
	public void addItem(Section section)
	{
		sections.add(section);
		notifyDataSetChanged();
	}

	public boolean removeItem(String url)
	{
		for(int i = 0; i < sections.size(); i++)
		{
			Section s = sections.get(i);
			if(s.getUrl().equals(url))
			{
				sections.remove(i);
				notifyDataSetChanged();
				return true;
			}
		}
		return false;
	}
	
	//发送图标到桌面
	private void addShortcut(String name, String sectionUrl)
	{
		Intent entryIntent = new Intent();
		entryIntent.setAction(ACTION_ENTER_BY_SHORTCUT);
		entryIntent.setClass(context, Main.class);
		entryIntent.putExtra("section_title", name);
		entryIntent.putExtra("url", sectionUrl);
		entryIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		
		
		Intent shortcutIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, entryIntent);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, R.drawable.ic_launcher);
		//只能创建一次
		shortcutIntent.putExtra("duplicate", false);
		context.sendBroadcast(shortcutIntent);
	}

	/**
	 * @param 0:不可见
	 */
	public void changeDelBtnState(int isVisble)
	{
		this.isVisible = isVisble;
		notifyDataSetChanged();
	}
	
	public boolean isEmpty()
	{
		return sections.isEmpty();
	}

	public Section getLastItem()
	{
		return sections.get(sections.size() - 1);
	}

	public boolean isFull()
	{
		return sections.size() >= Main.PAGE_SECTION_SIZE;
	}
}
