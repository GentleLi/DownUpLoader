package com.gentler.downuploader.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by administrato on 2017/6/13.
 */

public class ThreadPoolManager {

    private static ThreadPoolManager mThreadPoolManager;
    private int corePoolSize;
    private int maxPoolSize;
    private long keepAliveTime=30;
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    private ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolManager() {
        corePoolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;//核心线程数量
        maxPoolSize = corePoolSize;
        mThreadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, timeUnit, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    }

    public static ThreadPoolManager getInstance() {
        if (null == mThreadPoolManager) {
            synchronized (ThreadPoolManager.class) {
                mThreadPoolManager = new ThreadPoolManager();
            }
        }
        return mThreadPoolManager;
    }


    public void execute(Runnable runnable) {
        if (null == runnable) return;
        mThreadPoolExecutor.execute(runnable);
    }

    public void remove(Runnable runnable) {//暂停任务
        if (null == runnable) return;
        mThreadPoolExecutor.remove(runnable);
    }

}
