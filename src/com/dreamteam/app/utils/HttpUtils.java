package com.dreamteam.app.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpStatus;

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
		if(conn.getResponseCode() == HttpStatus.SC_OK)
		{
			return conn.getInputStream();
		}
		return null;
	}
	
	public static void disConnect()
	{
		if(null != conn)
		{
			conn.disconnect();
		}
	}
}
