package com.dgroup.simplersswidget.core.async;

import android.os.AsyncTask;

import com.dgroup.simplersswidget.app.RSSWidgetApplication;

import java.util.concurrent.ThreadPoolExecutor;

public abstract class CustomAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

    @SuppressWarnings("unchecked")
    public final AsyncTask<Params, Progress, Result> executeOnCustomExecutor(Params... params) {
        ThreadPoolExecutor threadPoolExecutor = RSSWidgetApplication.getInstance().getThreadPoolExecutor();
        if (threadPoolExecutor == null) {
            return null;
        }
        executeOnExecutor(threadPoolExecutor, params);
        return this;
    }
}
