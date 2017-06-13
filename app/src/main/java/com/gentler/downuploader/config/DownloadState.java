package com.gentler.downuploader.config;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloadState {

    public static int DOWNLOADING = 2;//下载中
    public static int WAITING = 1;//等待中
    public static int ERROR = -1;//下载出错
    public static int UNDO = -2;//取消下载
    public static int SUCCESS = 0;//下载成功

}
