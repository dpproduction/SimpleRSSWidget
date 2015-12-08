package com.dgroup.simplersswidget.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;

import com.dgroup.simplersswidget.R;
import com.dgroup.simplersswidget.app.RSSWidgetApplication;
import com.dgroup.simplersswidget.constants.AppConstants;
import com.dgroup.simplersswidget.ui.activity.ConfigActivity;
import com.dgroup.simplersswidget.ui.widget.WidgetService;
import com.dgroup.simplersswidget.util.Utils;

public class RSSWidgetProvider extends AppWidgetProvider {

    private static final String ACTION_SETTINGS = "action_settings";
    private static final String ACTION_NEXT = "action_next";
    private static final String ACTION_PREV = "action_prev";
    private static final String ACTION_OPEN_URL = "action_open_url";

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        Utils.deleteFromPref(context, AppConstants.RSS_URL, appWidgetIds[0]);
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
        super.onDisabled(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AppConstants.MINUTE, AppConstants.MINUTE, pi);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

        ComponentName thisWidget = new ComponentName(context,
                RSSWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            remoteViews.setRemoteAdapter(R.id.page_flipper, intent);
            remoteViews.setEmptyView(R.id.page_flipper, android.R.id.empty);

            //adapter item click
            Intent itemClickIntent = new Intent(context, RSSWidgetProvider.class);
            itemClickIntent.setAction(ACTION_OPEN_URL);
            PendingIntent itemClickPIntent = PendingIntent
                    .getBroadcast(context, widgetId, itemClickIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.page_flipper, itemClickPIntent);

            //settings click
            Intent configIntent = new Intent(context, ConfigActivity.class);
            configIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent
                    .getActivity(context, widgetId, configIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.settings, pendingIntent);

            //next click
            Intent nextIntent = new Intent(context, RSSWidgetProvider.class);
            nextIntent.setAction(RSSWidgetProvider.ACTION_NEXT);
            nextIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent nextPendingIntent = PendingIntent
                    .getBroadcast(context, widgetId, nextIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.next, nextPendingIntent);

            //prev click
            Intent prevIntent = new Intent(context,
                    RSSWidgetProvider.class);
            prevIntent.setAction(RSSWidgetProvider.ACTION_PREV);

            prevIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
            PendingIntent prevPendingIntent = PendingIntent
                    .getBroadcast(context, widgetId, prevIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.prev, prevPendingIntent);

            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }

    public static void updateStatus(int widgetId, boolean isError, boolean isLoading) {

        RemoteViews remoteViews = new RemoteViews(RSSWidgetApplication.getInstance().getPackageName(),
                R.layout.widget_layout);
        if (isError) {
            remoteViews.setViewVisibility(R.id.page_flipper, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.error, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.error, View.GONE);
        }
        if (isLoading) {
            remoteViews.setViewVisibility(R.id.page_flipper, View.INVISIBLE);
            remoteViews.setViewVisibility(R.id.progress, View.VISIBLE);
        } else {
            remoteViews.setViewVisibility(R.id.progress, View.GONE);
        }
        if (!isError && !isLoading) {
            remoteViews.setViewVisibility(R.id.page_flipper, View.VISIBLE);
        }

        AppWidgetManager.getInstance(RSSWidgetApplication.getInstance()).updateAppWidget(widgetId, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();

        int widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        switch (action) {
            case ACTION_SETTINGS:
                Intent starter = new Intent(context, ConfigActivity.class);
                starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(starter);
                break;
            case ACTION_NEXT:
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                    RemoteViews rv = new RemoteViews(context.getPackageName(),
                            R.layout.widget_layout);
                    rv.showNext(R.id.page_flipper);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    appWidgetManager.partiallyUpdateAppWidget(widgetId, rv);
                }
                break;
            case ACTION_PREV:
                if (widgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                    RemoteViews rv = new RemoteViews(context.getPackageName(),
                            R.layout.widget_layout);
                    rv.showPrevious(R.id.page_flipper);

                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
                    appWidgetManager.partiallyUpdateAppWidget(widgetId, rv);
                }
                break;
            case ACTION_OPEN_URL:
                String url = intent.getStringExtra(AppConstants.RSS_URL);
                Intent startBrowser = new Intent(Intent.ACTION_VIEW);
                startBrowser.setData(Uri.parse(url));
                startBrowser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startBrowser);
                break;

        }
    }

    public static void syncData(int... ids) {
        for (int widgetId : ids) {
            AppWidgetManager.getInstance(RSSWidgetApplication.getInstance()).notifyAppWidgetViewDataChanged(widgetId,
                    R.id.page_flipper);
        }
    }

}
