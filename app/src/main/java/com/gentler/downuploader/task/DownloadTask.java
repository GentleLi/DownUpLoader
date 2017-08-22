package com.gentler.downuploader.task;

import android.util.Log;

import com.gentler.downuploader.config.DownloadState;
import com.gentler.downuploader.config.Storage;
import com.gentler.downuploader.database.DBManager;
import com.gentler.downuploader.manager.DownloaderManager;
import com.gentler.downuploader.model.DownloadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 下载任务类
 * Created by Jiantao on 2017/6/12.
 */

public class DownloadTask implements Runnable {

    private static final String TAG = DownloadTask.class.getSimpleName();
    private DownloadInfo downloadInfo;
    private long startPos;
    private long stopPos;

    public DownloadTask(DownloadInfo downloadInfo) {
        this.downloadInfo = downloadInfo;
    }

    /**
     * 处理下载暂停
     */
    public void onDownloadPause() {//暂停时调用此方法
        DBManager.getInstance(DownloaderManager.getContext()).addDownloadInfo(downloadInfo);//将下载信息存储到数据库中
    }

    /**
     * 处理下载失败
     */
    public void onDownloadError() {//TODO 删除掉本地的资源
        DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
        DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);

    }

    @Override
    public void run() {
        File fileDir = new File(Storage.DOWNLOAD_DIR);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, downloadInfo.getName());//下载存放的文件
        Log.e(TAG, "file.length():" + file.length());
        Log.e(TAG, "downloadInfo.getCurrPos():" + downloadInfo.getCurrPos());
        try {
            if (file.exists()) {//文件存在
                if (file.length() != downloadInfo.getCurrPos()) {//如果文件不存在 或者文件长度为0 或者文件的长度与当前标记的下载长度不相等 则删除文件重新下载
                    file.delete();
                    file.createNewFile();
                    downloadInfo.setCurrPos(0);
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            downloadInfo.setCurrState(DownloadState.DOWNLOADING);
            startPos = downloadInfo.getCurrPos();
            URL url = new URL(downloadInfo.getDownloadUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60 * 1000);
            connection.setRequestProperty("Range", "bytes=" + startPos + "-" + downloadInfo.getSize());
            Log.e(TAG, "connection.getContentLength()==" + connection.getContentLength());
            Log.e(TAG, "connection.getResponseCode()==" + connection.getResponseCode());
            stopPos = connection.getContentLength();
            if (stopPos <= 0) {//下载出错，资源不存在
                downloadInfo.setCurrState(DownloadState.ERROR);
            }
            connection.connect();

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPos);
            InputStream is = null;
            if (connection.getResponseCode() == 206) {
                is = connection.getInputStream();
                int count;
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1 && (downloadInfo.getCurrState() == DownloadState.DOWNLOADING || downloadInfo.getCurrState() == DownloadState.RESTART)) {//判断当前状态是否是正在下载
                    randomAccessFile.write(buffer, 0, count);
                    downloadInfo.setCurrPos(downloadInfo.getCurrPos() + count);
                    DownloaderManager.getInstance().notifyDownloadProgressChanged(downloadInfo);
                    //下载过程中判断下载的状态 如果下载暂停 将下载信息存储到数据库中
                    if (file.length() == downloadInfo.getSize()) {
                        downloadInfo.setCurrState(DownloadState.SUCCESS);
                        DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
                        DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);//移除下载任务
                    }
                }
                if (downloadInfo.getCurrState() == DownloadState.PAUSE) {//暂停中
                    downloadInfo.setCurrState(DownloadState.PAUSE);
                    DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
                    onDownloadPause();
                } else if (downloadInfo.getCurrState() == DownloadState.ERROR) {//下载失败
                    onDownloadError();
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (is != null) {
                    is.close();
                }
                if (connection != null) {
                    connection.disconnect();
                    connection = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
