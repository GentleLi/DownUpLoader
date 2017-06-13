package com.gentler.downuploader.model;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class DownloadInfo {
    private String id;
    private String name;
    private String downloadUrl;
    private long size;
    private long currPos;
    private int currState;
    private String path;

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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
                ", path='" + path + '\'' +
                '}';
    }
}
