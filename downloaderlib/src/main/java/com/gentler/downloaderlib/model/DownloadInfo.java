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
    private int currState = DownloadState.IDLE;//当前未下载状态 初始化状态
    private String dir;//表示当前下载文件的文件夹

    private DownloadInfo(Builder info) {
        this.id = info.id;
        this.name = info.name;
        this.size = info.size;
        this.currState = info.currState;
        this.dir = info.dir;
        this.downloadUrl = info.downloadUrl;
        this.currPos = info.currPos;
    }

    public static class Builder {
        private String id;
        private String name;
        private String downloadUrl;
        private long size;
        private long currPos;
        private int currState = DownloadState.IDLE;//当前未下载状态 初始化状态
        private String dir;//表示当前下载文件的文件夹


        public Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder downloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
            return this;
        }

        public Builder currPos(long currPos) {
            this.currPos = currPos;
            return this;
        }

        public Builder size(long size) {
            this.size = size;
            return this;
        }

        public Builder currState(int currState) {
            this.currState = currState;
            return this;
        }

        public Builder dir(String dir) {
            this.dir = dir;
            return this;
        }

        public DownloadInfo build() {
            return new DownloadInfo(this);
        }
    }

    public void setCurrPos(long currPos) {
        this.currPos = currPos;
    }

    public void setCurrState(int currState) {
        this.currState = currState;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public long getSize() {
        return size;
    }

    public long getCurrPos() {
        return currPos;
    }

    public int getCurrState() {
        return currState;
    }

    public String getDir() {
        return dir;
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
