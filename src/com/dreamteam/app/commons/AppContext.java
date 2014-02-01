package com.dreamteam.app.commons;

import java.io.File;

import com.dreamteam.app.utils.FileUtils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.webkit.CacheManager;

/**
 * @description TODO
 * @author zcloud
 * @date Dec 8, 2013
 */
@SuppressWarnings("deprecation")
public class AppContext extends Application
{
	
	public static boolean isNetworkAvailable(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); 
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if(networkInfo == null || networkInfo.isConnected() == false)
		{
			return false;
		}
		return true;
	}

	public static SharedPreferences getPrefrences(Context context)
	{
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	//清楚缓存
	public void clearCache()
	{
		//sd
		File sdCache = new File(AppConfig.APP_CACHE_DIR);
		FileUtils.clearCache(sdCache);
		clearWebViewCache();
	}
	
	//清除webview缓存
	public void clearWebViewCache()
	{
		File file = CacheManager.getCacheFileBaseDir();  
		if (file != null && file.exists() && file.isDirectory()) {  
		    for (File item : file.listFiles()) {  
		    	item.delete();  
		    }  
		    file.delete();  
		}  		  
		deleteDatabase("webview.db");  
		deleteDatabase("webview.db-shm");  
		deleteDatabase("webview.db-wal");  
		deleteDatabase("webviewCache.db");  
		deleteDatabase("webviewCache.db-shm");  
		deleteDatabase("webviewCache.db-wal");  
	}	
}
