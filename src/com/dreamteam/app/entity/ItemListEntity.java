package com.dreamteam.app.entity;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.dreamteam.app.commons.ItemListEntityParser;

@SuppressWarnings("serial")
public class ItemListEntity implements Serializable
{
	private ArrayList<FeedItem> itemList = new ArrayList<FeedItem>();

	public ArrayList<FeedItem> getItemList()
	{
		return itemList;
	}

	public void setItemList(ArrayList<FeedItem> itemList)
	{
		this.itemList = itemList;
	}
	
	public ItemListEntity parse(InputStream inputStream)
	{
		ItemListEntityParser ile = new ItemListEntityParser();
		return ile.parse(inputStream);
	}
}
