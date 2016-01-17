package com.dreamteam.app.utils;

import com.dreateam.app.ui.R;

import android.content.Context;


/**
 * @description	TODO
 * @author zcloud
 * @date 2014年1月27日
 */
public class CategoryNameExchange
{
	private String[] cates_zh;
	private String[] cates_en;
	
	public CategoryNameExchange(Context context)
	{
		cates_zh = context.getResources()
				.getStringArray(R.array.feed_category);
		cates_en = context.getResources()
				.getStringArray(R.array.feed_category_en);
	}
	
	public String zh2en(String zh)
	{
		for(int i = 0; i < cates_zh.length; i++)
		{
			if(zh.equals(cates_zh[i]))
				return cates_en[i];
		}
		return null;
	}
	
	public String en2zh(String en)
	{
		for(int i = 0; i < cates_en.length; i++)
		{
			if(en.equals(cates_en[i]))
				return cates_zh[i];
		}
		return null;
	}
}
