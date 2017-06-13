package com.gentler.downuploader.config;

import android.os.Environment;

import java.io.File;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class Storage {

    private static String ROOT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();//根目录
    public static String DOWNLOAD_DIR = ROOT_DIR + File.separator + "download" + File.separator;//下载目录
    public static String FILE_DIR = ROOT_DIR + File.separator + "file" + File.separator;//文件目录
    public static String CACHE_DIR = ROOT_DIR + File.separator + "cache" + File.separator;//缓存目录


}
