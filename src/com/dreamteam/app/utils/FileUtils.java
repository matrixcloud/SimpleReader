package com.dreamteam.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.os.Environment;

import com.dreamteam.app.commons.AppConfig;

/**
 * @author: zcloud
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
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			return Environment.getExternalStorageDirectory() + "";
		} else
		{
			return null;
		}
	}

	public static void createDir(String path, String dirName)
	{
		File file = new File(path + dirName);
		file.mkdirs();
	}

	public static void createFile(String path, String fileName)
	{
		File file = new File(path + fileName);
		try
		{
			file.createNewFile();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public static boolean isFileExists(String path, String fileName)
	{
		File file = new File(path + fileName);
		return file.exists();
	}

	/**
	 * @param in
	 * @param path
	 * @param fileName
	 * @return boolean
	 */
	public static boolean writeToFile(InputStream in, String path,
			String fileName)
	{
		FileOutputStream out = null;
		byte buffer[] = new byte[4 * 1024];

		File file = new File(path, fileName);
		try
		{
			file.createNewFile();
			out = new FileOutputStream(file);
			while ((in.read(buffer)) != -1)
			{
				out.write(buffer);
			}
			return true;
		} catch (IOException e1)
		{
			e1.printStackTrace();
			return false;
		} finally
		{
			if (null != out)
			{
				try
				{
					out.flush();
					out.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
			if (null != in)
			{
				try
				{
					in.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

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
		String fileName = AppConfig.APP_SECTION_DIR + File.separator
				+ MD5.Md5(url);
		File file = new File(fileName);
		return file;
	}

	public static File UrlToFile(String url)
	{
		String fileName = AppConfig.APP_SECTION_DIR + File.separator
				+ MD5.Md5(url);
		return new File(fileName);
	}
	
	public static File createSectionCacheFile(String url)
	{
		String fileName = AppConfig.APP_SECTION_DIR + File.separator
				+ MD5.Md5(url);
		File file = new File(fileName);

		file.delete();
		new File(file.getParent()).mkdirs();
		try
		{
			file.createNewFile();
		} catch (IOException e)
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
		String fileName = AppConfig.APP_IMAGE_CACHE_DIR + File.separator
				+ MD5.Md5(url);
		File file = new File(fileName);
		return file;
	}

	/**
	 * 获取目录文件大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir)
	{
		if (dir == null)
		{
			return 0;
		}
		if (!dir.isDirectory())
		{
			return 0;
		}
		long dirSize = 0;
		File[] files = dir.listFiles();
		for (File file : files)
		{
			if (file.isFile())
			{
				dirSize += file.length();
			} else if (file.isDirectory())
			{
				dirSize += file.length();
				dirSize += getDirSize(file); // 递归调用继续统计
			}
		}
		return dirSize;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS)
	{
		java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024)
		{
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576)
		{
			fileSizeString = df.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824)
		{
			fileSizeString = df.format((double) fileS / 1048576) + "MB";
		} else
		{
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

}
