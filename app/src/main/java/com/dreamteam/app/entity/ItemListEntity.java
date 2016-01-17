package com.dreamteam.app.entity;

import java.io.Serializable;
import java.util.ArrayList;

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
	
	public FeedItem getFirstItem()
	{
		return itemList.get(0);
	}
}
