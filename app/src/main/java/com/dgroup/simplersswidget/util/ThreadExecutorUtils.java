package com.dgroup.simplersswidget.util;

import android.support.annotation.NonNull;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadExecutorUtils {

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 2;
    private static final int KEEP_ALIVE = 10;
    private static final int MAX_QUEUE_SIZE  = 128;

    public static ThreadPoolExecutor getOptimalThreadPool(){
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(MAX_QUEUE_SIZE), getThreadFactory());
    }

    private static ThreadFactory getThreadFactory(){
        return new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r, "Task #" + mCount.getAndIncrement());
            }
        };
    }

}
