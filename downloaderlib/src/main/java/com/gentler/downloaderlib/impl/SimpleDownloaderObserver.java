package com.gentler.downloaderlib.impl;


import com.gentler.downloaderlib.base.BaseDownloaderObserver;
import com.gentler.downloaderlib.model.DownloadInfo;

/**
 * Created by Jiantao on 2017/8/24.
 */

public class SimpleDownloaderObserver extends BaseDownloaderObserver {



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
