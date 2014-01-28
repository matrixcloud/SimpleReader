package com.dreamteam.app.db;

import android.content.Context;
import android.database.Cursor;
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
	public static final String DB_NAME = "reader.db";
	public static final String SECTION_TABLE_NAME = "section";
	public static final String FAVORITE_ITEM_TABLE_NAME = "favorite_item";
	
	public static final String CREATE_SECTION =
				"create table" + " " + SECTION_TABLE_NAME + "(title text, url text, table_name text)";
	public static final String CREATE_FAVORITE_ITEM = 
				"create table" + " " + FAVORITE_ITEM_TABLE_NAME + "(title text, pubdate text, item_detail text)";

	
	
	public DBHelper(Context context, String name, CursorFactory factory,
			int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(CREATE_SECTION);
		db.execSQL(CREATE_FAVORITE_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
	}
	
}
