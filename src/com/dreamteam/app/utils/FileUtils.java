package com.dreamteam.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

import com.dreamteam.app.commons.AppConfig;

/**
 * @author:	zcloud
 *
 * @description: sd操作
 *
 * @date: 2013/10/9
 *
 */

public class FileUtils
{
	public static String getSDRootPath()
	{
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
		{
			return Environment.getExternalStorageDirectory() + "";
		}
		else
		{
			return null;
		}
	}
	
	//����Ŀ¼
	public static void createDir(String path, String dirName)
	{
		File file = new File(path + dirName);
		file.mkdirs();
	}
	
	//�����ļ�
	public static void createFile(String path, String fileName)
	{
		File file = new File(path + fileName);
		try 
		{
			file.createNewFile();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	//�ж��ļ��Ƿ����
	public static boolean isFileExists(String path, String fileName)
	{
		File file = new File(path + fileName);
		return file.exists();
	}
	
	/**
	 * @param in
	 * @param path
	 * @param fileName
	 * @return	boolean
	 */
	public static boolean writeToFile(InputStream in, String path, String fileName)
	{
		FileOutputStream out = null;
		byte buffer [] = new byte[4 * 1024];
		
		File file = new File(path, fileName);
		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);
			while((in.read(buffer)) != -1)
			{
				out.write(buffer);
			}
			return true;
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
			return false;
		}
		finally
		{
			if(null != out)
			{
				try
				{
					out.flush();
					out.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
			if(null != in)
			{
				try
				{
					in.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	//ɾ���ļ�
	public static boolean deleteFile(String path, String fileName)
	{
		File file = new File(path + fileName);
		return file.delete();
	}
	
	public static String urlToPath(String url)
	{
		return MD5.Md5(url);
	}
	
	public static boolean clearCache(File file)
	{
		return file.delete();
	}
	
	/**
	 * @description TODO
	 * @param url
	 * @return File
	 */
	public static File getSectionCacheFile(String url)
	{
		String fileName = AppConfig.APP_SECTION_DIR + File.separator + MD5.Md5(url);
		File file = new File(fileName);
		return file;
	}
	
	public static File createSectionCacheFile(String url)
	{
		String fileName = AppConfig.APP_SECTION_DIR + File.separator + MD5.Md5(url);
		File file = new File(fileName);
		
		file.delete();
		new File(file.getParent()).mkdirs();
		try
		{
			file.createNewFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * @description TODO
	 * @param url
	 * @return File
	 */
	public static File getImageSDFile(String url)
	{
		String fileName = AppConfig.APP_IMAGE_CACHE_DIR + File.separator + MD5.Md5(url);
		File file = new File(fileName);
		return file;
	}
	
}
