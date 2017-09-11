package com.gentler.downuploader;

import android.app.Application;
import android.content.Context;

import com.gentler.downloaderlib.manager.DownloaderManager;
import com.gentler.downuploader.utils.Utils;

/**
 * Created by Jiantao on 2017/6/12.
 */

public class MyApp extends Application {

    private Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getApplicationContext();
        Utils.init(mContext);
        DownloaderManager.init(mContext);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();

    }


    @Override
    public void onTerminate() {
        super.onTerminate();

    }


}
