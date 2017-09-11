package com.gentler.downloaderlib.interfaces;


import com.gentler.downloaderlib.model.DownloadInfo;

/**
 * Created by Jiantao on 2017/8/24.
 */

public interface IDownloaderObserver {

    void onDownloadPause(DownloadInfo downloadInfo);

    void onDownloadProgressChanged(DownloadInfo downloadInfo);

    void onDownloadSuccess(DownloadInfo downloadInfo);

    void onDownloadError(DownloadInfo downloadInfo);

}
