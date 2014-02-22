package com.dreamteam.app.commons;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description 过滤html
 * @author zcloud
 * @date 2013/11/14
 */
public class HtmlFilter
{
	//过滤所有标签
	public static final String regexpForHtml = "<([^>]*)>";
	//过滤img标签
	public static final String regexpForImg = "(<|;)\\s*(IMG|img)\\s+([^;^>]*)\\s*(;|>)";
	//获取img标签的url
	public static final String regexpForImgUrl = "http://([^\"]+)\"";
	//过滤<>中的style
	public static final String regexpForStyle = "\\s*style=\"([^\"]*)\"";
	
	
	/**
	 * @description
	 * @param input
	 * @return String
	 */
	public static String filterHtml(String input)
	{
		Pattern pattern = Pattern.compile(regexpForHtml);
		Matcher matcher = pattern.matcher(input);
		StringBuffer sb = new StringBuffer();
		while(matcher.find())
		{
			matcher.appendReplacement(sb, "");
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	public static ArrayList<String> getImgTags(String input)
	{
		Pattern pattern = Pattern.compile(regexpForImg);
		Matcher matcher = pattern.matcher(input);
		ArrayList<String> imgTags = new ArrayList<String>();
		
		while(matcher.find())
		{
			imgTags.add(matcher.group());
		}
		return imgTags;
	}
	
	public static ArrayList<String> getImageSrcs(String input)
	{
		ArrayList<String> srcs = new ArrayList<String>();
		
		Pattern tagPattern = Pattern.compile(regexpForImg);
		Matcher tagMatcher = tagPattern.matcher(input);
		Pattern srcPattern = Pattern.compile(regexpForImgUrl);
		
		while(tagMatcher.find())
		{
			Matcher srcMatcher = srcPattern.matcher(tagMatcher.group());
			while(srcMatcher.find())
			{
				String src = srcMatcher.group().replace("\"", "");
				srcs.add(src);
			}
		}
		return srcs;
	}
}
