package com.gentler.downuploader.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by administrato on 2017/6/13.
 */

public class SPUtils {

    /**
     * 保存文件长度
     * @param context
     * @param fileLength
     */
    private static void saveFileLength(Context context ,Long fileLength ){
        SharedPreferences sp = context.getSharedPreferences("My_SP" , Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong("File_startOffset" , fileLength);
        editor.commit();
    }
    /**
     * 获取文件长度
     * @param context
     * @return
     */
    private static Long getFileLength(Context context){
        SharedPreferences sp = context.getSharedPreferences("My_SP" , Context.MODE_PRIVATE);
        return sp.getLong("File_startOffset" , 0);
    }

}
