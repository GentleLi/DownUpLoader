package com.gentler.downuploader.impl;

import android.widget.ProgressBar;

import com.gentler.downuploader.base.BaseDownloaderObserver;
import com.gentler.downuploader.model.DownloadInfo;

/**
 * Created by administrato on 2017/8/24.
 */

public class SimpleDownloaderObserver extends BaseDownloaderObserver {

    private ProgressBar mProgressBar;

    public void bindProgressBar(ProgressBar progressBar){
        this.mProgressBar=progressBar;
    }

    public SimpleDownloaderObserver(String id) {
        super(id);
    }

    @Override
    public void onDownloadPause(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadProgressChanged(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadSuccess(DownloadInfo downloadInfo) {

    }

    @Override
    public void onDownloadError(DownloadInfo downloadInfo) {

    }


}
