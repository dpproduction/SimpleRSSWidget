package com.dgroup.simplersswidget.receiver;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

import com.dgroup.simplersswidget.R;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("WidgetExample","AlarmManagerBroadcastReceiver onReceive");
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, RSSWidgetProvider.class.getName());

        wl.acquire();

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        remoteViews.setTextViewText(R.id.update, DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());

        ComponentName rssWidget = new ComponentName(context, RSSWidgetProvider.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(rssWidget, remoteViews);

        wl.release();
    }
}
