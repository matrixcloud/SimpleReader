package com.dreamteam.app.ui;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.dreateam.app.ui.R;

public class WidgetProvider extends AppWidgetProvider
{
	public static final String tag = "WidgetProvider";
	public static final String ACTION_WIDGET_PLAY = "com.dreamteam.action.widget.play";
	
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Log.i(tag, "--------->>onReceive()");
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds)
	{
		Log.i(tag, "--------->>onUpdate()");
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_4x1_layout);
		Intent intent = new Intent();
		intent.setAction(ACTION_WIDGET_PLAY);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
		rv.setOnClickPendingIntent(R.id.widget_btn_play, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, rv);
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds)
	{
		Log.i(tag, "--------->>onDeleted()");
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context)
	{
		Log.i(tag, "--------->>onEnabled()");
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context)
	{
		Log.i(tag, "--------->>onDisabled()");
		super.onDisabled(context);
	}

}
