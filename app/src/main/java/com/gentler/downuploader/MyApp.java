package com.gentler.downuploader;

import android.app.Application;

import com.gentler.downuploader.utils.Utils;

/**
 * Created by administrato on 2017/6/12.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
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
