package com.dreamteam.app.commons;

import com.dreateam.app.ui.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class UIHelper
{
	public static final String tag = "UIHelper";
	
	//webView的css
	public final static String WEB_STYLE = "<style type=\"text/css\">"
			+ "@font-face{ "
			+ "font-family:huawenxinwei;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-size:20px;line-height:25px;word-wrap:break-word;font-family:huawenxinwei;background-color:white;color:#0}"
			+ "p{text-indent:2em}"
			+ "img{max-width:310px} "
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration:none;color:#3E62A6}" 
			+ "h1{padding-top:1em;text-align:center;font-family:default;font-size:23px;line-height:30px}"
			+ "</style>";
	public final static String WEB_STYLE_NIGHT = "<style type=\"text/css\">"
			+ "@font-face{"
			+ "font-family:huawenxinwei;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-size:20px;line-height:25px;word-wrap:break-word;font-family:huawenxinwei;background-color:black;color:#aaaaaa}"
			+ "p{text-indent:2em}"
			+ "img{max-width:310px}"
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration: none;color:#3E62A6}" 
			+ "h1{padding-top:1em;text-align:center;font-family:default;color:#aaaaaa;font-size:23px;line-height:30px}"
			+ "</style>";
	
	
	/**
	 * @description 初始化activity的主题
	 * @param context
	 */
	public static void initTheme(Context context)
	{
		//true为日间模式
		boolean isNight = false;
		
		SharedPreferences prefs = AppContext.getPrefrences(context);
		isNight = prefs.getBoolean("day_night_mode", false);
		if(isNight)
		{
			Log.d(tag, "isNight");
			context.setTheme(R.style.AppNightTheme);
		}
	}
	
}
