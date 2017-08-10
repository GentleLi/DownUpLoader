package com.gentler.downuploader.config;

/**
 * Created by administrato on 2017/6/12.
 */

public class AppConfig {

    public static AppMode mCurrAppMode=AppMode.DEBUG;
    private static String mOffLineUrl="http://139.224.18.21/public/";
    private static String mOnLineUrl="http://api2.peppertv.cn/public/";
    private static String mBaseUrl=(mCurrAppMode==AppMode.DEBUG)? mOffLineUrl:mOnLineUrl;//根据状态判断地址
}
