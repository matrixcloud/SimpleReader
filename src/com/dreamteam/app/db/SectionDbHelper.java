package com.dreamteam.app.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SectionDbHelper
{
	public static void removeRecoder(SQLiteDatabase db, String url)
	{
		db.delete(DbManager.SECTION_TABLE_NAME, "url=?", new String[]{url});
		db.close();
	}
	
	public static void insert(SQLiteDatabase db, String tableName, String title, String url)
	{
		ContentValues values = new ContentValues();
		values.put("table_name", tableName);
		values.put("title", title);
		values.put("url", url);
		db.insert(DbManager.SECTION_TABLE_NAME, null, values);
		db.close();
	}
}
