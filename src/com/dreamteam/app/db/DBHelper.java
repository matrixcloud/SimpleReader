package com.dreamteam.app.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @description reader.db助手
 * @author zcloud
 * @date 2014年1月24日
 */
public class DBHelper extends SQLiteOpenHelper
{
	public static final String CREATE_FAVORITE_FEED =
				"create table section(title text, url text, category_id integer)";
	public static final String CREATE_FAVORITE_ITEM = 
				"create table favorite_item(title text, pubdate text, item_detail text)";
	
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_FAVORITE_FEED);
		db.execSQL(CREATE_FAVORITE_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
}
