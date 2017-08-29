package com.gentler.downuploader.manager;


import android.content.Context;
import android.text.TextUtils;

import com.gentler.downuploader.base.BaseDownloaderObserver;
import com.gentler.downuploader.config.DownloadState;
import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.task.DownloadTask;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloaderManager {
    private static final String TAG=DownloaderManager.class.getSimpleName();
    private static DownloaderManager mDownloaderManager;
    private static Context mContext;
    private ConcurrentHashMap<String,BaseDownloaderObserver> mDownloaderObservers=new ConcurrentHashMap<>();
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

    public static void init(Context context){
        mContext=context;
    }

    public static Context getContext(){
        if (null==mContext){
            throw new NullPointerException("Oops! Context is null,have you initialize DownloadManager in your app's application ?");
        }
        return mContext;
    }

    /**
     * 暂停单个下载任务
     * @param downloadInfo
     */
    public synchronized void pause(DownloadInfo downloadInfo){//暂停下载任务
        if (null!=mDownloadTaskMap){
            DownloadTask downloadTask=mDownloadTaskMap.get(downloadInfo.getId());
            downloadInfo.setCurrState(DownloadState.PAUSE);//设置下载状态为暂停
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



    public void registerObserver(BaseDownloaderObserver observer) {
        if (observer != null && !mDownloaderObservers.contains(observer)) {
            mDownloaderObservers.put(observer.getId(),observer);
        }
    }

    public void unregisterObserver(BaseDownloaderObserver observer) {
        if (observer != null && mDownloaderObservers.contains(observer)) {
            mDownloaderObservers.remove(observer);
        }
    }

    public void unregisterObserver(String targetId){
        if (TextUtils.isEmpty(targetId))return;
        mDownloaderObservers.remove(targetId);
    }

    public void notifyDownloadPause(DownloadInfo downloadInfo) {
//        mDownloaderObservers.keySet().forEach((String key)->{
//            if (downloadInfo.getId().equals(key)){
//                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
//                observer.onDownloadPause(downloadInfo);
//            }
//        });
        for(String key:mDownloaderObservers.keySet()){
            if (downloadInfo.getId().equals(key)){
                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
                observer.onDownloadPause(downloadInfo);
            }
        }
    }

    public void notifyDownloadSuccess(DownloadInfo downloadInfo){
        if (null==downloadInfo){
            throw new NullPointerException("Oops! downloadInfo is null");
        }
//        mDownloaderObservers.keySet().forEach((String key)->{
//            if (downloadInfo.getId().equals(key)){
//                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
//                observer.onDownloadSuccess(downloadInfo);
//            }
//        });
        for(String key:mDownloaderObservers.keySet()){
            if (downloadInfo.getId().equals(key)){
                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
                observer.onDownloadSuccess(downloadInfo);
            }
        }
    }

    public void notifyDownloadError(DownloadInfo downloadInfo){

//        if (null==downloadInfo){
//            throw new NullPointerException("Oops! downloadInfo is null");
//        }
//        mDownloaderObservers.keySet().forEach((String key)->{
//            if (downloadInfo.getId().equals(key)){
//                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
//                observer.onDownloadError(downloadInfo);
//            }
//        });
        for(String key:mDownloaderObservers.keySet()){
            if (downloadInfo.getId().equals(key)){
                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
                observer.onDownloadError(downloadInfo);
            }
        }

    }


    public void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
        if (null==downloadInfo){
            throw new NullPointerException("Oops! downloadInfo is null");
        }
//        mDownloaderObservers.keySet().forEach((String key)->{
//            if (downloadInfo.getId().equals(key)){
//                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
//                observer.onDownloadProgressChanged(downloadInfo);
//            }
//        });
        for(String key:mDownloaderObservers.keySet()){
            if (downloadInfo.getId().equals(key)){
                BaseDownloaderObserver observer=mDownloaderObservers.get(key);
                observer.onDownloadProgressChanged(downloadInfo);
            }
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

    public boolean isTargetExists(String targetId){
        return mDownloadInfoMap.containsKey(targetId);
    }

}
