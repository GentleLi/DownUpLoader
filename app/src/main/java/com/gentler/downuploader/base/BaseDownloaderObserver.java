package com.gentler.downuploader.base;

import com.gentler.downuploader.interfaces.IDownloaderObserver;

/**
 * Created by Jiantao on 2017/8/24.
 */

public abstract class BaseDownloaderObserver implements IDownloaderObserver{

    protected String id;
    public BaseDownloaderObserver(String id) {
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseDownloaderObserver that = (BaseDownloaderObserver) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
