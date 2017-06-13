package com.gentler.downuploader.task;

import android.util.Log;


import com.gentler.downuploader.config.Storage;
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
        try {
            URL url = new URL("download url");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setUseCaches(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(60 * 1000);
            connection.setRequestProperty("Range", "bytes=" + downloadInfo.getCurrPos() + "-" + downloadInfo.getSize());
            connection.connect();
            stopPos = connection.getContentLength();
            Log.e(TAG, "connection.getContentLength()==" + connection.getContentLength());
            Log.e(TAG, "connection.getResponseCode()==" + connection.getResponseCode());
            File file = new File(Storage.DOWNLOAD_DIR + downloadInfo.getName());
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            randomAccessFile.seek(startPos);
            InputStream is = null;
            if (connection.getResponseCode() == 206) {
                is = connection.getInputStream();
                int count = 0;
                byte[] buffer = new byte[1024];
                while ((count = is.read(buffer)) != -1) {
                    randomAccessFile.write(buffer, 0, count);
                }
                if (randomAccessFile != null) {
                    randomAccessFile.close();
                }
                if (is != null) {
                    is.close();
                }
                startPos = stopPos + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
