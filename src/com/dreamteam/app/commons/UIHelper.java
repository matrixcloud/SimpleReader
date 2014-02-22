package com.dreamteam.app.commons;

public class UIHelper
{
	//webView的css
	public final static String WEB_STYLE = "<style type=\"text/css\">"
			+ "@font-face{ "
			+ "font-family:myface;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-family:myface;background-color:white}"
			+ "p{font-size:20px;line-height:25px;}"
			+ "p{color:#0;text-indent:2em}"
			+ "img{max-width:310px} "
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration:none;color:#3E62A6}" 
			+ "h1{font-size:23px;line-height:30px}"
			+ "</style>";
	public final static String WEB_STYLE_NIGHT = "<style type=\"text/css\">"
			+ "@font-face{ "
			+ "font-family:myface;"
			+ "font-weight:normal;"
			+ "font-style:normal;"
			+ "src:url('file:///android_asset/huawenxinwei.TTF') format('truetype');"
			+ "}"
			+ "body{font-family:myface;background-color:black}"
			+ "p{font-size:20px;line-height:25px;}"
			+ "p{color:#aaaaaa;text-indent:2em}"
			+ "img{max-width:310px} "
			+ "p img{display:block;margin:0 auto}"
			+ "a{text-decoration: none;color:#3E62A6}" 
			+ "h1{color:#aaaaaa;font-size:20px}"
			+ "</style>";
}
