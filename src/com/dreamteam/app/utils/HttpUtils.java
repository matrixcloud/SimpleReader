package com.dreamteam.app.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author:	zcloud
 *
 * @description: TODO
 *
 * @date: 2013/10/10
 *
 */
public class HttpUtils
{
	private static HttpURLConnection conn = null;

	private static final int TIME_OUT = 10000;

	public static final String tag = "HttpUtils";
	
	/**
	 * @param url
	 * @return InputStream
	 * @throws Exception
	 */
	public static InputStream getInputStream(String url) throws Exception
	{
		URL httpURL = null;
		
		httpURL = new URL(url);
		conn = (HttpURLConnection) httpURL.openConnection();
		return conn.getInputStream();
		
		//		conn.setRequestMethod("GET");
////		conn.setReadTimeout(TIME_OUT);
//		if(HttpURLConnection.HTTP_OK == conn.getResponseCode())
//		{
//			return conn.getInputStream();
//		}
//		else
//		{
//			disConnect();
//			return null;
//		}
	}
	
	public static void disConnect()
	{
		if(null != conn)
		{
			conn.disconnect();
		}
	}
	
	
}
