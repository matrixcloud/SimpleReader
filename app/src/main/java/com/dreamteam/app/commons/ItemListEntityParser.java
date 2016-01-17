package com.dreamteam.app.commons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import com.dreamteam.app.entity.FeedItem;
import com.dreamteam.app.entity.ItemListEntity;
import com.dreamteam.app.utils.DateUtils;
import com.dreamteam.app.utils.HttpUtils;

public class ItemListEntityParser extends DefaultHandler
{
	private String tag = "ItemListEntityParser";
	private ItemListEntity itemListEntity;
	private FeedItem feedItem;
	private ArrayList<FeedItem> items = new ArrayList<FeedItem>();
	private StringBuffer sb = new StringBuffer();
	private boolean isFeedTitle = true;
	private boolean isFeedDesc = true;
	private boolean isFeedLink = true;
	private String feedTitle;
	
	
	@Override
	public void startDocument() throws SAXException
	{
		Log.i(tag, "开始解析");
		itemListEntity = new ItemListEntity();
	}
	
	@Override
	public void endDocument() throws SAXException 
	{
		Log.i(tag, "结束解析");
		itemListEntity.setItemList(items);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{	
		sb.setLength(0);
		if(qName.equalsIgnoreCase("item"))
		{
			feedItem = new FeedItem();
			items.add(feedItem);
			isFeedTitle = false;
			isFeedDesc = false;
			isFeedLink = false;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		String content = sb.toString();
		if(!isFeedLink && qName.equalsIgnoreCase("link"))
		{
			feedItem.setLink(content);
		}
		if(qName.equalsIgnoreCase("title"))
		{
			if(isFeedTitle)
			{
				feedTitle = content;
			}
			else
			{
				feedItem.setTitle(content);
			}
			return;
		}
		if(!isFeedDesc && (qName.equalsIgnoreCase("description") || qName.equalsIgnoreCase("content:encoded")))
		{
			feedItem.setContent(content);
			ArrayList<String> srcs = HtmlFilter.getImageSrcs(content);
			if(!srcs.isEmpty())	
				feedItem.setFirstImageUrl(srcs.get(0));
			feedItem.setImageUrls(srcs);
			isFeedDesc = false;
			return;
		}
		if(qName.equalsIgnoreCase("pubDate"))
		{
			content = DateUtils.rfcNormalDate(content);
			if(feedItem != null)
				feedItem.setPubdate(content);
		}
	}
		
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException 
	{
		sb.append(ch, start, length);
	}

	/**
	 * @description TODO
	 * @param inputStream
	 * @return ItemListEntity
	 */
	public ItemListEntity parse(String url)
	{
		SAXParserFactory saxpf = SAXParserFactory.newInstance();
		SAXParser saxp = null;
		InputStream inputStream = null;
		InputSource inputSource = null;
		
		try
		{
			inputStream = HttpUtils.getInputStream(url);
//			String encoding = HtmlFilter.getEncoding(inputStream);
//			Log.d(tag, encoding);
//			InputStreamReader isr = new InputStreamReader(inputStream, encoding);不可用
			inputSource = new InputSource(inputStream);
			saxp = saxpf.newSAXParser();
			XMLReader xmlr = saxp.getXMLReader();
			xmlr.setContentHandler(this);
			xmlr.parse(inputSource);
			return itemListEntity;
		}
		catch(ParserConfigurationException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(SAXException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
			return null;
		}
		catch(IOException e)
		{
			e.printStackTrace();
			return null;
		} catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		finally
		{
			if(inputStream != null)
			{
				try
				{
					inputStream.close();
					inputStream = null;
				} catch (IOException e)
				{
					e.printStackTrace();
				}
				
			}
		}
	}

	public String getFeedTitle() {
		return feedTitle;
	}

}
