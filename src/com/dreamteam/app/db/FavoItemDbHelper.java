package com.dreamteam.app.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class FavoItemDbHelper
{
	public static void insert(SQLiteDatabase db, String title, 
			String pubdate, String itemDetail, String link, 
			String firstImgUrl, String sectionTitle)
	{
		ContentValues values = new ContentValues();
		values.put("title", title);
		values.put("pubdate", pubdate);
		values.put("item_detail", itemDetail);
		values.put("link", link);
		values.put("first_img_url", firstImgUrl);
		values.put("table_name", sectionTitle);
		db.insert("favorite_item", null, values);
		db.close();
	}

	public static void removeRecord(SQLiteDatabase db, String link)
	{
		db.delete(DbManager.FAVORITE_ITEM_TABLE_NAME, "link=?", new String[]{link});
		db.close();
	}
}
