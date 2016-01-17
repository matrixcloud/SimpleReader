package com.dreamteam.app.utils;


/**
 * @description 
 * @author zcloud
 * @date 2013/11/13
 */
public class DateUtils
{

	/**
	 * @description rfc实例：Tue, 08 Oct 2013 18:02:03 +0800��
	 * 转为yy/mm/day
	 * @param date
	 * @return String
	 */
	public static String rfcNormalDate(String date)
	{
		String[] strs = date.split("\\s+|:");
		
		return enNumberMonth(strs[2])
					   + "月" + strs[1] + "日" + ' ' + strs[4]
					   + ":" + strs[5];
	}
	
	private static int enNumberMonth(String month)
	{
		String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
				"Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
		
		for(int i = 0; i < months.length; i++)
		{
			if(months[i].equalsIgnoreCase(month))
				return i + 1;
		}
		return 0;
	}
}