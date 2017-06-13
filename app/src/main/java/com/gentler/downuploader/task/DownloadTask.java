package com.gentler.downuploader.task;

import android.util.Log;

import com.gentler.downuploader.config.DownloadState;
import com.gentler.downuploader.config.Storage;
import com.gentler.downuploader.manager.DownloaderManager;
import com.gentler.downuploader.model.DownloadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;


/**
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

    @Override
    public void run() {
        File fileDir=new File(Storage.DOWNLOAD_DIR);
        if (!fileDir.exists()){
            fileDir.mkdirs();
        }
        File file = new File(fileDir,downloadInfo.getName());
        if (!file.exists()||file.length()!=downloadInfo.getCurrPos()||file.length()==0){//如果文件不存在 或者文件长度为0 或者文件的长度与当前标记的下载长度不相等 则删除文件重新下载
            file.delete();
            downloadInfo.setCurrPos(0);
        }
        try {
            downloadInfo.setCurrState(DownloadState.DOWNLOADING);
            URL url = new URL(downloadInfo.getDownloadUrl());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60 * 1000);
            Log.e(TAG, "connection.getContentLength()==" + connection.getContentLength());
            Log.e(TAG, "connection.getResponseCode()==" + connection.getResponseCode());
            stopPos = connection.getContentLength();
            connection.setRequestProperty("Range", "bytes=" + downloadInfo.getCurrPos() + "-" + downloadInfo.getSize());
            connection.connect();

            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPos);
            InputStream is = null;
            if (connection.getResponseCode() == 206) {
                is = connection.getInputStream();
                int count;
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1&&downloadInfo.getCurrState()== DownloadState.DOWNLOADING) {//判断当前状态是否是正在下载
                    randomAccessFile.write(buffer, 0, count);
                    downloadInfo.setCurrPos(downloadInfo.getCurrPos()+count);
                    DownloaderManager.getInstance().notifyDownloadProgressChanged(downloadInfo);
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (is != null) {
                    is.close();
                }
                if (connection!=null){
                    connection.disconnect();
                    connection=null;
                }
                Log.e(TAG,"下载完成");
//                startPos = stopPos + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (file.length()==downloadInfo.getSize()){
            downloadInfo.setCurrState(DownloadState.SUCCESS);
            DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
            DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);//移除下载任务
            //TODO　存储临时下载信息到数据库中
        }else if(downloadInfo.getCurrState()==DownloadState.PAUSE){//暂停中
            downloadInfo.setCurrState(DownloadState.PAUSE);
            DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
            //TODO 储存下载临时信息到数据库中
        }else{//下载失败
            downloadInfo.setCurrState(DownloadState.ERROR);
            DownloaderManager.getInstance().notifyDownloadStateChanged(downloadInfo);
            DownloaderManager.getInstance().removeSingleDownloadTask(downloadInfo);
        }


    }


}
