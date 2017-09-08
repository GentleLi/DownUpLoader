package com.gentler.downuploader.impl;

import android.util.Log;
import android.widget.ProgressBar;

import com.gentler.downuploader.base.BaseDownloaderObserver;
import com.gentler.downuploader.model.DownloadInfo;

/**
 * Created by Jiantao on 2017/9/8.
 */

public class BarDownloaderObserver extends BaseDownloaderObserver {

    private static final String TAG = BarDownloaderObserver.class.getSimpleName();
    private ProgressBar mProgressBar;
    private final int MAX = 1000;

    public void bindProgressBar(ProgressBar progressBar) {
        if (null!=mProgressBar){
            Log.e(TAG,"progressbar :  old to new ");
        }else{
            Log.d(TAG,"progressbar : first set ");
        }
        this.mProgressBar = progressBar;
        mProgressBar.setMax(1000);
    }

    public ProgressBar getProgressBar() {
        return mProgressBar;
    }

    public BarDownloaderObserver(String id) {
        super(id);
    }

    @Override
    public void onDownloadPause(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {
        if (null != mProgressBar){
            mProgressBar.setProgress((int) (downloadInfo.getCurrPos() * 1.0 * MAX / downloadInfo.getSize()));
        }
    }

    @Override
    public void onDownloadSuccess(DownloadInfo downloadInfo) {
        Log.e(TAG, "onDownloadSuccess");

    }

    @Override
    public void onDownloadError(DownloadInfo downloadInfo) {
        Log.e(TAG, "onDownloadError");
    }
}
