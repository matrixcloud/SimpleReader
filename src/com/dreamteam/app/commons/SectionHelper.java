package com.dreamteam.app.commons;

import java.io.File;
import java.io.IOException;

import com.dreamteam.app.db.DbManager;
import com.dreamteam.app.utils.MD5;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SectionHelper
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

	public static File newSdCache(String url)
	{
		File cache = AppContext.getSdImgCache(url);
		try
		{
			cache.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return cache;
	}

	public static File getSdCache(String url)
	{
		return new File(AppConfig.APP_SECTION_DIR + MD5.Md5(url));
	}
}
