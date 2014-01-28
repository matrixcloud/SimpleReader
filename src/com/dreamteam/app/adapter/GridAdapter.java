package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dreamteam.app.entity.Section;
import com.dreateam.app.ui.R;

public class GridAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Section> sections;
	
	
	public GridAdapter(Context context, ArrayList<Section> sections)
	{
		this.context = context;
		this.sections = sections;
	}
	
	
	@Override
	public int getCount()
	{
		return sections.size() + 8;
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
	public View getView(int position, View convertView, ViewGroup parent)
	{
//		ViewHolder holder = null;
//		
//		if(convertView == null)
//		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.section_item, null);
//			holder = new ViewHolder();
//			holder.itemTitle = (TextView) convertView.findViewById(R.id.item_text);
//			convertView.setTag(holder);
//		}
//		else
//		{
//			holder = (ViewHolder) convertView.getTag();
//		}
//		
//		Section section = sections.get(position);
//		if(section != null)
//		{
//			holder.itemTitle.setText(section.getTitle());
//		}
		return view;
	}
	
	private static final class ViewHolder
	{
		TextView itemTitle;
	}
	
	public void addItem(Section section)
	{
		sections.add(section);
		notifyDataSetChanged();
	}


	public void removeItem(String url)
	{
		for(int i = 0; i < sections.size(); i++)
		{
			Section s = sections.get(i);
			if(s.getUrl().equals(url))
			{
				sections.remove(i);
			}
		}
		notifyDataSetChanged();
	}
}
