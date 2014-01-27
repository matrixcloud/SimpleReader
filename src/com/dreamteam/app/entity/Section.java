package com.dreamteam.app.entity;

public class Section
{
	private String title;
	private String url;
	private String FirstItemDetail;
	private String tableName;//所属数据库表
	
	
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getUrl()
	{
		return url;
	}
	public void setUrl(String url)
	{
		this.url = url;
	}
	public String getFirstItemDetail()
	{
		return FirstItemDetail;
	}
	public void setFirstItemDetail(String firstItemDetail)
	{
		FirstItemDetail = firstItemDetail;
	}
	public String getTableName()
	{
		return tableName;
	}
	public void setTableName(String tableName)
	{
		this.tableName = tableName;
	}
}
