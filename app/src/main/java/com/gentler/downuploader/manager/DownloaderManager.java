package com.gentler.downuploader.manager;


import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.task.DownloadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloaderManager {

    private static DownloaderManager mDownloaderManager;
    private List<DownloaderObserver> mDownloaderObservers = new ArrayList<>();
    private ConcurrentHashMap<String,DownloadInfo> mDownloadInfoMap;

    private DownloaderManager() {
        mDownloadInfoMap=new ConcurrentHashMap<>();
    }

    public static DownloaderManager getInstance() {
        if (null == mDownloaderManager) {
            synchronized (DownloaderManager.class) {
                if (null == mDownloaderManager) {
                    mDownloaderManager = new DownloaderManager();
                }
            }
        }
        return mDownloaderManager;
    }


    public interface DownloaderObserver {

        void onDownloadStateChanged(DownloadInfo downloadInfo);

        void onDownloadProgressChanged(DownloadInfo downloadInfo);
    }

    public void registerObserver(DownloaderObserver observer) {
        if (observer != null && !mDownloaderObservers.contains(observer)) {
            mDownloaderObservers.add(observer);
        }
    }

    public void unregisterObserver(DownloaderObserver observer) {
        if (observer != null && mDownloaderObservers.contains(observer)) {
            mDownloaderObservers.remove(observer);
        }
    }

    public void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
        for (DownloaderObserver observer : mDownloaderObservers) {
            observer.onDownloadStateChanged(downloadInfo);
        }
    }

    public void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
        for (DownloaderObserver observer : mDownloaderObservers) {
            observer.onDownloadProgressChanged(downloadInfo);
        }
    }

    public synchronized void download(DownloadInfo downloadInfo) {
        DownloadTask mDownloadTask = new DownloadTask(downloadInfo);
        mDownloadInfoMap.put(downloadInfo.getId(),downloadInfo);//将下载的DownloadInfo放入Map中
        ThreadPoolManager.getInstance().execute(mDownloadTask);
    }

}
