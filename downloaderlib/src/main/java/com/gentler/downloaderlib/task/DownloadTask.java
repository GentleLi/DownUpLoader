package com.gentler.downloaderlib.task;


import android.util.Log;

import com.gentler.downloaderlib.config.DownloadState;
import com.gentler.downloaderlib.database.DBManager;
import com.gentler.downloaderlib.helper.DownloadHelper;
import com.gentler.downloaderlib.manager.DownloaderManager;
import com.gentler.downloaderlib.manager.ThreadPoolManager;
import com.gentler.downloaderlib.model.DownloadInfo;

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
        Log.d(TAG,"onDownloadPause 将下载信息存储到数据库");
        if (DBManager.getInstance(DownloaderManager.getContext()).has(downloadInfo)){
            DBManager.getInstance(DownloaderManager.getContext()).update(downloadInfo);
        }else{
            DBManager.getInstance(DownloaderManager.getContext()).addDownloadInfo(downloadInfo);//将下载信息存储到数据库中
        }
        ThreadPoolManager.getInstance().remove(this);
    }

    /**
     * 处理下载失败
     */
    public void onDownloadError() {//TODO 删除掉本地的资源
        DownloaderManager.getInstance().notifyDownloadError(downloadInfo);
        DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);
        DBManager.getInstance(DownloaderManager.getContext()).update(downloadInfo);//更新数据库，下载出错
    }

    @Override
    public void run() {
        String tempFile= DownloadHelper.getTempFilePath(downloadInfo.getDir(),downloadInfo.getName());
        File file = new File(tempFile);//下载存放的文件


        Log.e(TAG, "downloadInfo.getCurrPos():" + downloadInfo.getCurrPos());
        try {
            if (file.exists()) {//文件存在
                Log.d(TAG,"file.length():"+file.length());
                if (file.length()==0||file.length() != downloadInfo.getCurrPos()) {//如果文件不存在 或者文件长度为0 或者文件的长度与当前标记的下载长度不相等 则删除文件重新下载
                    file.delete();
                    file.createNewFile();
                    downloadInfo.setCurrPos(0);
                }
            } else {
                downloadInfo.setCurrPos(0);//如果文件不存在，则重新将开始位置设为0
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
            if (stopPos < 0) {//下载出错，资源不存在
                downloadInfo.setCurrState(DownloadState.ERROR);
            }
            connection.connect();
            Log.e(TAG, "connection.getContentLength()==" + connection.getContentLength());
            Log.e(TAG, "connection.getResponseCode()==" + connection.getResponseCode());
            stopPos = connection.getContentLength();

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rwd");
            randomAccessFile.seek(startPos);
            InputStream is = null;
            if (connection.getResponseCode() == 206) {
                is = connection.getInputStream();
                int count;
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1 && (downloadInfo.getCurrState()==DownloadState.IDLE||downloadInfo.getCurrState() == DownloadState.DOWNLOADING || downloadInfo.getCurrState() == DownloadState.RESTART)) {//判断当前状态是否是正在下载
                    randomAccessFile.write(buffer, 0, count);
                    downloadInfo.setCurrPos(downloadInfo.getCurrPos() + count);
                    DownloaderManager.getInstance().notifyDownloadProgressChanged(downloadInfo);
                    //下载过程中判断下载的状态 如果下载暂停 将下载信息存储到数据库中
                    if (downloadInfo.getCurrPos() == downloadInfo.getSize()) {
                        downloadInfo.setCurrState(DownloadState.SUCCESS);
                        String oldFilePath=DownloadHelper.getTempFilePath(downloadInfo.getDir(),downloadInfo.getName());
                        DownloadHelper.renameTo(oldFilePath);
                        DownloaderManager.getInstance().notifyDownloadSuccess(downloadInfo);
                        DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);//移除下载任务
                    }
                }
                if (downloadInfo.getCurrState() == DownloadState.PAUSE) {//暂停中
                    Log.d(TAG,"download pause!");
                    downloadInfo.setCurrState(DownloadState.PAUSE);
                    DownloaderManager.getInstance().notifyDownloadPause(downloadInfo);
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
            downloadInfo.setCurrState(DownloadState.ERROR);
            onDownloadError();
        }finally{

        }
    }


}
