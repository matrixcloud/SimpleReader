package com.dreamteam.app.ui;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dreamteam.app.adapter.SectionGridAdapter;
import com.dreamteam.app.db.DBHelper;
import com.dreamteam.app.entity.Section;
import com.dreateam.app.ui.R;

public class MFragment extends Fragment
{
	private ArrayList<Section> sections = new ArrayList<Section>();
	private SectionGridAdapter gridAdapter;
	private int page;//所在页码
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		Bundle args = getArguments();
		page = args.getInt("page");
		View view = inflater.inflate(R.layout.section_grid, null);
		GridView gridView = (GridView) view.findViewById(R.id.section_grid);
		try
		{
			readSections();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		//设置适配器
		gridAdapter = new SectionGridAdapter(getActivity(), sections);
		gridView.setAdapter(gridAdapter);
		return view;
	}
	
	private void readSections() throws Exception
	{
		System.out.println("--------------->>readTable()");
		int len = 0;//表长
		int start = 0;//其实读
		int end = 0;//结尾
		
		//从数据库读数据
		DBHelper helper = new DBHelper(getActivity(), "reader.db", null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("section", null, null, null, null, null, null);
		
		len = cursor.getCount();
		start = page * Main.PAGE_SECTION_SIZE;
		System.out.println("start = " + start);
		if (cursor.moveToPosition(start))
		{
			int offset = start + Main.PAGE_SECTION_SIZE;
			end = len < offset ? len : offset;
			System.out.println("end = " + end);
			for (int i = start; i < end; i++)
			{
				Section s = new Section();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				s.setTitle(title);
				s.setUrl(url);
				sections.add(s);
				cursor.moveToNext();
			}
		}
		db.close();
	}
}
