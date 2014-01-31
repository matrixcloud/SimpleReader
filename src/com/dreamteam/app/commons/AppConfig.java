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
	//SD卡目录
	public static final String APP_ROOT_DIR = FileUtils.getSDRootPath() + File.separator + "SimpleReader";
	public static final String APP_CACHE_DIR = APP_ROOT_DIR + File.separator + "cache";
	public static final String APP_SECTION_DIR = APP_CACHE_DIR + File.separator + "sections";
	public static final String APP_IMAGE_CACHE_DIR = APP_CACHE_DIR + File.separator + "images";
	public static final String APP_IMAGE_DIR = APP_ROOT_DIR + File.separator + "images";
	//文章过期配置
	public static final String PREF_DEPRECATED = "pref_deprecated";
	
	
	
	
	//有盟
	public static final String UM_BASE_KEY = "com.dreamteam.reader";
	
}
