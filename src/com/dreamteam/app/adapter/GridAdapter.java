package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamteam.app.commons.SectionHelper;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.entity.Section;
import com.dreamteam.app.ui.Main;
import com.dreateam.app.ui.R;

public class GridAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Section> sections;
	//deleteButton是否可见
	private int isVisible = 0;//deleteButton是否可见
	private int[] visibleStates = {View.GONE,View.VISIBLE};
	
	
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
			holder.delteBtn = (ImageView) convertView.findViewById(R.id.item_btn_delte);
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
				//删除当前section
				sections.remove(position);
				notifyDataSetChanged();
				//移除数据库中的记录
				DbManager mgr = new DbManager(context, DbManager.DB_NAME, null, 1);
				SectionHelper.removeRecoder(mgr.getWritableDatabase(), section.getUrl());
				//移除缓存
				 
			}
		});
		holder.delteBtn.setVisibility(visibleStates[isVisible]);
		return convertView;
	}
	
	private static final class ViewHolder
	{
		TextView itemTitle;
		ImageView delteBtn;
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
