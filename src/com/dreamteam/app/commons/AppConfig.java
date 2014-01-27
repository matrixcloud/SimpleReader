/**
 * 
 */
package com.dreamteam.app.commons;

import java.io.File;

import com.dreamteam.app.utils.FileUtils;

/**
 * @author:	zcloud
 *
 * @description: TODO
 *
 * @date: 2013/10/11
 *
 */
public class AppConfig
{
	//sd��
	public static final String APP_ROOT_DIR = FileUtils.getSDRootPath() + File.separator + "SimpleReader";
	public static final String APP_CACHE_DIR = APP_ROOT_DIR + File.separator + "cache";
	public static final String APP_SECTION_DIR = APP_CACHE_DIR + File.separator + "sections";
	public static final String APP_IMAGE_CACHE_DIR = APP_CACHE_DIR + File.separator + "images";
	public static final String APP_IMAGE_DIR = APP_ROOT_DIR + File.separator + "images";
	
	public static final String FEED_FILE_NAME = APP_ROOT_DIR + File.separator + "favorite.feed";
	
	public static final String SECTION_TABLE_NAME = "section";
	public static final String FEED_TABLE_NAME = "feed";

	public static final int ITEM_PAGE_SIZE = 10;
	
	public static final String FEED_SRC_FILE_NAME = APP_ROOT_DIR + File.separator + "feed.data";
	public static final String SECTION_FILE_NAME = APP_ROOT_DIR + File.separator + "favorite.feed";
	
	//Action
	public static final String SPEECH_ACTION = "com.dreamteam.reader.SPEECH_ACTION";
	
}
