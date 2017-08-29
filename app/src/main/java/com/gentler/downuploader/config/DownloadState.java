package com.gentler.downuploader.config;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloadState {

    public static final int DOWNLOADING = 2;//下载中
    public static final int RESTART = 1;//开始下载，重新开始下载
    public static final int PAUSE = -3;//等待中
    public static final int ERROR = -1;//下载出错
    public static final int UNDO = -2;//取消下载
    public static final int SUCCESS = 0;//下载成功
    public static final int IDLE = 3;//为开始下载状态


}
