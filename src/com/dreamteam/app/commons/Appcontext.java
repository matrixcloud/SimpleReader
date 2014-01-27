package com.dreamteam.app.commons;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * @description TODO
 * @author zcloud
 * @date Dec 8, 2013
 */
public class Appcontext extends Application
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
}
