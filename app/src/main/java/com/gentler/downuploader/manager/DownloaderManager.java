package com.gentler.downuploader.manager;


import com.gentler.downuploader.model.DownloadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloaderManager {

    private static DownloaderManager mDownloaderManager;
    private List<DownloaderObserver> mDownloaderObservers = new ArrayList<>();


    private DownloaderManager() {
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

    public void notifyDownloadeProgressChanged(DownloadInfo downloadInfo){
        for(DownloaderObserver observer:mDownloaderObservers){
            observer.onDownloadProgressChanged(downloadInfo);
        }
    }

    public synchronized void download(){

    }


}
