package com.dreamteam.app.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


public class FavoItemDbHelper
{	
	public static final String TITLE = "title";
	public static final String PUBDATE = "pubdate";
	public static final String DETAIL = "item_detail";
	public static final String LINK = "link";
	public static final String FIRST_IMG_URL = "first_img_url";
	public static final String SECTION_TITLE = "table_name";
	public static final String SECTION_URL = "table_url";
	
	
	public static void insert(SQLiteDatabase db, String title, 
			String pubdate, String itemDetail, String link, 
			String firstImgUrl, String sectionTitle, String sectionUrl)
	{
		ContentValues values = new ContentValues();
		values.put(TITLE, title);
		values.put(PUBDATE, pubdate);
		values.put(DETAIL, itemDetail);
		values.put(LINK, link);
		values.put(FIRST_IMG_URL, firstImgUrl);
		values.put(SECTION_TITLE, sectionTitle);
		values.put(SECTION_URL, sectionUrl);
		
		db.insert(DbConstant.FAVO_TABLE_NAME, null, values);
		db.close();
	}

	public static void removeRecord(SQLiteDatabase db, String link)
	{
		db.delete(DbManager.FAVORITE_ITEM_TABLE_NAME, "link=?", new String[]{link});
		db.close();
	}
}
