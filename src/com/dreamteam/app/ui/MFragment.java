package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dreamteam.app.adapter.SectionGridAdapter;
import com.dreamteam.app.entity.Section;
import com.dreateam.app.ui.R;

public class MFragment extends Fragment
{
	private ArrayList<Section> sections = new ArrayList<Section>();
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.section_grid, null);
		GridView gridView = (GridView) view.findViewById(R.id.section_grid);
		for(int i = 0; i < 8; i++)
		{
			sections.add(new Section());
		}
		SectionGridAdapter gridAdapter = new SectionGridAdapter(getActivity(), sections);
		gridView.setAdapter(gridAdapter);
		return view;
	}
}
