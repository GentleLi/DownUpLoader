package com.gentler.downloaderlib.model;


import com.gentler.downloaderlib.config.DownloadState;

/**
 * 下载信息类，包含要下载文件的各种信息
 * Created by Jiantao on 2017/6/12.
 */

public class DownloadInfo {
    private String id;
    private String name;
    private String downloadUrl;
    private long size;
    private long currPos;
    private int currState= DownloadState.IDLE;//当前未下载状态 初始化状态
    private String dir;//表示当前下载文件的文件夹

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getCurrPos() {
        return currPos;
    }

    public void setCurrPos(long currPos) {
        this.currPos = currPos;
    }

    public int getCurrState() {
        return currState;
    }

    public void setCurrState(int currState) {
        this.currState = currState;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String toString() {
        return "DownloadInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", size=" + size +
                ", currPos=" + currPos +
                ", currState=" + currState +
                ", dir='" + dir + '\'' +
                '}';
    }
}
