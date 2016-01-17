package com.dreamteam.app.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dreamteam.app.db.DbConstant;
import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.entity.Section;
import com.dreamteam.app.ui.Main;

/**
 * @description TODO
 * @author zcloud
 * @date 2014年9月11日
 */
public class SectionDao
{
	public static final String tag = "SectionDAO";
	private Context context;
	
	
	public SectionDao(Context context)
	{
		this.context = context;
	}
	
	public ArrayList<Section> getList(int page)
	{
		ArrayList<Section> list = null;
		int len = 0;// 表长
		int start = 0;// 其实读
		int end = 0;// 结尾
		Log.i(tag, "page = " + page);
		// 从数据库读数据
		DbManager mgr = new DbManager(context, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbConstant.SECTION_TABLE_NAME, 
								null, null, null, null, null, null);
		len = cursor.getCount();
		db.close();
		
		start = page * Main.PAGE_SECTION_SIZE;
		if (cursor.moveToPosition(start))
		{
			list = new ArrayList<Section>();

			int offset = start + Main.PAGE_SECTION_SIZE;
			end = len < offset ? len : offset;
			for (int i = start; i < end; i++)
			{
				Section s = new Section();
				String title = cursor.getString(cursor.getColumnIndex("title"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				String tableName = cursor.getString(cursor.getColumnIndex("table_name"));
				s.setTitle(title);
				s.setUrl(url);
				s.setTableName(tableName);
				list.add(s);
				cursor.moveToNext();
			}
		}
		return list;
	}
	
	public Section getLast()
	{
		Section section = new Section();
		
		DbManager mgr = new DbManager(context, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbConstant.SECTION_TABLE_NAME, null, null, null,
				null, null, null);
		if (cursor.moveToLast())
		{
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String url = cursor.getString(cursor.getColumnIndex("url"));
			String tableName = cursor.getString(cursor
					.getColumnIndex("table_name"));
			section.setTitle(title);
			section.setUrl(url);
			section.setTableName(tableName);
		}
		cursor.close();
		db.close();
		return section;
	}
	
	public int getCount()
	{
		// 从数据库读数据
		DbManager mgr = new DbManager(context, DbManager.DB_NAME, null, 1);
		SQLiteDatabase db = mgr.getWritableDatabase();
		Cursor cursor = db.query(DbConstant.SECTION_TABLE_NAME, 
					null, null, null, null, null, null);
		int count = cursor.getCount();
		cursor.close();
		db.close();
		return count;
	}
}
