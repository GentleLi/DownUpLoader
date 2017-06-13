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
    private static ConcurrentHashMap<String,DownloadInfo> mDownloadInfoMap;
    private static ConcurrentHashMap<String,DownloadTask> mDownloadTaskMap;
    static{
        mDownloadInfoMap=new ConcurrentHashMap<>();
        mDownloadTaskMap=new ConcurrentHashMap<>();
    }
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

    /**
     * 暂停单个下载任务
     * @param downloadInfo
     */
    public synchronized void pause(DownloadInfo downloadInfo){//暂停下载任务
        if (null!=mDownloadTaskMap){
            DownloadTask downloadTask=mDownloadTaskMap.get(downloadInfo.getId());
            ThreadPoolManager.getInstance().remove(downloadTask);
        }
    }

    /**
     * 移除单个下载任务
     * @param downloadInfo
     */
    public void removeSingleDownloadTask(DownloadInfo downloadInfo){
        if (null==downloadInfo||null==mDownloadInfoMap)return;
        if (mDownloadInfoMap.contains(downloadInfo.getId())){
            mDownloadInfoMap.remove(downloadInfo.getId());
        }
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

    /**
     * 放入队列，开启下载任务
     * @param downloadInfo
     */
    public synchronized void download(DownloadInfo downloadInfo) {
        DownloadTask downloadTask = new DownloadTask(downloadInfo);
        mDownloadInfoMap.put(downloadInfo.getId(),downloadInfo);//将下载的DownloadInfo放入Map中
        ThreadPoolManager.getInstance().execute(downloadTask);
        mDownloadTaskMap.put(downloadInfo.getId(),downloadTask);
    }

}
