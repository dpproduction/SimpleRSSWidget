package com.dgroup.simplersswidget.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dgroup.simplersswidget.constants.AppConstants;

public class Utils {

    public static void saveToPref(Context context, int appWidgetId, String key, String value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(key+appWidgetId, value).commit();
    }

    public static String loadFromPref(Context context, String key, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key+appWidgetId, "");
    }

    public static void deleteFromPref(Context context, String key, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.remove(key+appWidgetId).commit();
    }

}
