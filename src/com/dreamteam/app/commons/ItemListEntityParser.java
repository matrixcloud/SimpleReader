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
	
	
	@Override
	public void startDocument() throws SAXException
	{
		Log.i(tag, "startDocument()");
		itemListEntity = new ItemListEntity();
	}
	
	@Override
	public void endDocument() throws SAXException 
	{
		Log.i(tag, "endDocument()");
		itemListEntity.setItemList(items);
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
	{	
		sb.setLength(0);
		if(localName.equalsIgnoreCase("item"))
		{
			feedItem = new FeedItem();
			items.add(feedItem);
			isFeedTitle = false;
			isFeedDesc = false;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException
	{
		String content = sb.toString();
		
		if(!isFeedTitle && localName.equalsIgnoreCase("title"))
		{
			feedItem.setTitle(content);
			return;
		}
		if(!isFeedDesc && localName.equalsIgnoreCase("description"))
		{
			feedItem.setDescription(content);
			ArrayList<String> srcs = HtmlFilter.getImageSrcs(content);
			if(!srcs.isEmpty())	
				feedItem.setFirstImageUrl(srcs.get(0));
			feedItem.setImageUrls(srcs);
			isFeedDesc = false;
			return;
		}
		if(localName.equalsIgnoreCase("pubDate"))
		{
			content = DateUtils.rfcNormalDate(content);
			if(feedItem != null)
				feedItem.setPubdate(content);
		}
		if(qName.equalsIgnoreCase("content:encoded"))
		{
			feedItem.setContentEncoded(content);
			ArrayList<String> srcs = HtmlFilter.getImageSrcs(content);
			if(!srcs.isEmpty())	
				feedItem.setFirstImageUrl(srcs.get(0));
			feedItem.setImageUrls(srcs);
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
		InputStream inputStream = null;
		SAXParser saxp = null;
		SAXParserFactory saxpf = SAXParserFactory.newInstance();
		
		try
		{
			inputStream = HttpUtils.getInputStream(url);
			saxp = saxpf.newSAXParser();
			XMLReader xmlr = saxp.getXMLReader();
			xmlr.setContentHandler(this);
			InputSource inputSource = new InputSource(inputStream);
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
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
}
