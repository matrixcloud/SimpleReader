package com.dreamteam.app.entity;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 1.title
 * 2.description
 * 3.link
 * 4.pubdate
 * 
 * */

@SuppressWarnings("serial")
public class FeedItem implements Serializable
{
	private String title;
	private String description;
	private String link;
	private String pubdate;
	private String category;
	private String firstImageUrl;
	private String contentEncoded;
	private boolean readed = false;
	private boolean favorite = false;
	private ArrayList<String> imageUrls = new ArrayList<String>();
	
	
	public String getFirstImageUrl()
	{
		return firstImageUrl;
	}
	public void setFirstImageUrl(String imageUrl)
	{
		this.firstImageUrl = imageUrl;
	}
	
	public String getTitle() 
	{
		return title;
	}
	public void setTitle(String title) 
	{
		this.title = title;
	}
	public String getDescription() 
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getLink() 
	{
		return link;
	}
	public void setLink(String link)
	{
		this.link = link;
	}
	public String getPubdate() 
	{
		return pubdate;
	}
	public void setPubdate(String pubdate)
	{
		this.pubdate = pubdate;
	}
	public String getCategory() 
	{
		return category;
	}
	public void setCategory(String category)
	{
		this.category = category;
	}
	public String getContentEncoded()
	{
		return contentEncoded;
	}
	public void setContentEncoded(String contentEncoded)
	{
		this.contentEncoded = contentEncoded;
	}
	public ArrayList<String> getImageUrls()
	{
		return imageUrls;
	}
	public void setImageUrls(ArrayList<String> imageUrls)
	{
		this.imageUrls = imageUrls;
	}
	public boolean isReaded()
	{
		return readed;
	}
	public void setReaded(boolean readed)
	{
		this.readed = readed;
	}
	public boolean isFavorite()
	{
		return favorite;
	}
	public void setFavorite(boolean favorite)
	{
		this.favorite = favorite;
	}
	
}
