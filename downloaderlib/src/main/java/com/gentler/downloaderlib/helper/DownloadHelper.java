package com.gentler.downloaderlib.helper;

import android.util.Log;


import com.gentler.downloaderlib.config.Constants;

import java.io.File;

/**
 * Created by jiantao on 2017/9/7.
 */

public class DownloadHelper {
    private static final String TAG=DownloadHelper.class.getSimpleName();

    public static String getTempFilePath(String fileDir, String fileName) {
        return fileDir + fileName + Constants.TMP;
    }

    public static void renameTo(String oldFilePath){
        File oldFile=new File(oldFilePath);
        String newFilePath=oldFilePath.substring(0,oldFilePath.lastIndexOf("."));
        File newFile=new File(newFilePath);
        if (oldFile.exists()&&newFile.exists()){
            oldFile.renameTo(newFile);
            Log.d(TAG,oldFile.getName()+" rename to " +newFile.getName() +" success !!!");
        }
    }

}
