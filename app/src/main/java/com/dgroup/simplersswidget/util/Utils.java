package com.dgroup.simplersswidget.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.dgroup.simplersswidget.constants.AppConstants;

import java.util.ArrayList;

public class Utils {

    public static final int SECOND = 1000;

    public static final int MINUTE = 60 * SECOND;

    public static void saveToPref(Context context, int appWidgetId, String key, String value) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.putString(key+appWidgetId, value).commit();
    }

    public static String loadFromPref(Context context, int appWidgetId, String key) {
        SharedPreferences prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(key+appWidgetId, "");
    }

    public static void deleteFromPref(Context context, String key, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(AppConstants.PREFS_NAME, Context.MODE_PRIVATE).edit();
        prefs.remove(key+appWidgetId).commit();
    }

    public static void loadAllTitlePrefs(Context context, ArrayList<Integer> appWidgetIds,
                                  ArrayList<String> texts) {
    }

}
