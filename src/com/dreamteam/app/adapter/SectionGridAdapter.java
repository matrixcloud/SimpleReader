package com.dreamteam.app.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.dreamteam.app.entity.Section;
import com.dreateam.app.ui.R;

public class SectionGridAdapter extends BaseAdapter
{
	private Context context;
	private ArrayList<Section> sections = new ArrayList<Section>();
	
	
	public SectionGridAdapter(Context context, ArrayList<Section> sections)
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
	public View getView(int postion, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.section_item, null);
		return view;
	}
}
