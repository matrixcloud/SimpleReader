package com.dreamteam.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @description feed.db助手
 * @author zcloud
 * @date 2014年1月24日
 */
public class FeedDBManager extends SQLiteOpenHelper
{
	public static final String DB_NAME = "feed.db";
	
	
	public FeedDBManager(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{

	}

	/**
	 * @description 1为选中
	 * @param tableName
	 * @param state
	 * @param url
	 */
	public void updateState(String tableName, int state, String url)
	{
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("state", state);
		db.update(tableName, values, "url=?", new String[]{url});
		db.close();
	}

}
