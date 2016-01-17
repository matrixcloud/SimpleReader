package com.dreamteam.app.commons;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.iflytek.speech.SpeechUtility;

/**
 * @description	TODO
 * @author zcloud
 * @date 2014年2月22日
 */
public class IFlyHelper
{

	public static void openDownloadDialog(final Context context)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("安装之后可享受语音阅读功能").setTitle("是否安装讯飞语音+");
		builder.setPositiveButton("确定",
				new DialogInterface.OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						String url = SpeechUtility.getUtility(context).getComponentUrl();
						openDownloadWeb(context, url);
					}
				});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
		{
			@Override
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
	}
		
	// 判断手机中是否安装了讯飞语音+
	public static boolean checkSpeechServiceInstall(Context context)
	{
		String packageName = "com.iflytek.speechcloud";
		List<PackageInfo> packages = context.getPackageManager()
				.getInstalledPackages(0);
		for (int i = 0; i < packages.size(); i++)
		{
			PackageInfo packageInfo = packages.get(i);
			if (packageInfo.packageName.equals(packageName))
			{
				return true;
			} else
			{
				continue;
			}
		}
		return false;
	}

	/**
	 * 打开语音服务组件下载页面。
	 * 
	 * @param context
	 * @param url
	 */
	public static void openDownloadWeb(Context context, String url)
	{
		Uri uri = Uri.parse(url);
		Intent it = new Intent(Intent.ACTION_VIEW, uri);
		context.startActivity(it);
	}
}
