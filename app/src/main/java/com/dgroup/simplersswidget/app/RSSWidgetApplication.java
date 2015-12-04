package com.dgroup.simplersswidget.app;


import android.app.Application;

import com.dgroup.simplersswidget.util.ThreadExecutorUtils;

import java.util.concurrent.ThreadPoolExecutor;

public class RSSWidgetApplication extends Application{

    private static RSSWidgetApplication instance;

    private ThreadPoolExecutor mThreadPoolExecutor;

    public static RSSWidgetApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        mThreadPoolExecutor = ThreadExecutorUtils.getOptimalThreadPool();
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        if (mThreadPoolExecutor.isShutdown() || mThreadPoolExecutor.isTerminated()) {
            return null;
        }
        return mThreadPoolExecutor;
    }
}
