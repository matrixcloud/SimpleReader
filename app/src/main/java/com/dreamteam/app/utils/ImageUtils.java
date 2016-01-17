package com.dreamteam.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;

import com.dreamteam.app.commons.AppConfig;

/**
 * @author zcloud
 * @date 2013/11/13
 */
public class ImageUtils
{
	public static final CompressFormat mCompressForamat = CompressFormat.JPEG;
	public static final int mQuality = 70;
	
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h)
	{
		Bitmap newBitmap = null;
		if (bitmap != null)
		{
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			
			float scaleWidth = ((float) w/width);
			float scaleHeight = ((float) h/height);
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
		}
		return newBitmap;
	}
	
	
	public static void saveImageToSD(InputStream is, String path, String fileName)
	{
		FileUtils.saveToFile(is, path, fileName);
	}
	
	public static void saveImageToSD(Bitmap bmp, String url)
	{
		try
		{
			File file = FileUtils.newAbsoluteFile(AppConfig.APP_IMAGE_CACHE_DIR 
					+ File.separator + MD5.Md5(url));
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(mCompressForamat, mQuality, fos);
		}
		catch(IOException e1)
		{
			e1.printStackTrace();
		}
		
	}
	
	 public static int dip2px(Context context, float dpValue) 
	 {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (dpValue * scale + 0.5f);  
	  }  
	
	 public static int px2dip(Context context, float pxValue)
	 {  
	        final float scale = context.getResources().getDisplayMetrics().density;  
	        return (int) (pxValue / scale + 0.5f);  
	 }
}