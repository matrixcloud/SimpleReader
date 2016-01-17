package com.dreamteam.app.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.dreamteam.app.db.FeedDBManager;
import com.dreamteam.app.entity.Feed;

/**
 * @description TODO
 * @author zcloud
 * @date 2014年10月1日
 */
public class FeedDao
{
	public static final String TABLE_NAME = "feed";
	private Context context;
	
	public FeedDao(Context context)
	{
		this.context = context;
	}
	
	public ArrayList<Feed> getListByCategoryId(String id)
	{
		ArrayList<Feed> list = new ArrayList<Feed>();
		FeedDBManager helper = new FeedDBManager(context, FeedDBManager.DB_NAME, null, 1);
		SQLiteDatabase db = helper.getWritableDatabase();
		
		Cursor cursor = db.query(TABLE_NAME, null, "cid=?", new String[]{id}, null, null, null);
		if (cursor.moveToFirst())
		{
			for (int i = 0, n = cursor.getCount(); i < n; i++)
			{
				Feed f = new Feed();
				String title = cursor.getString(cursor.getColumnIndex("fname"));
				String url = cursor.getString(cursor.getColumnIndex("url"));
				int selectStatus = cursor.getInt(cursor
						.getColumnIndex("state"));
				f.setTitle(title);
				f.setUrl(url);
				f.setSelectStatus(selectStatus);
				list.add(f);
				cursor.moveToNext();
			}
		}
		cursor.close();
		db.close();
		return list;
	}
}
