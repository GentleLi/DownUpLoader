package com.gentler.downuploader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gentler.downuploader.config.AppConstants;
import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.utils.LogUtils;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by administrato on 2017/6/14.
 */

public class DBManager {
    private static final String TAG = DBManager.class.getSimpleName();
    private static DBManager mDatabaseManager;
    private final DBHelper mDatabaseHelper;

    private DBManager(Context context) {
        mDatabaseHelper = new DBHelper(context);
    }

    public static DBManager getInstance(Context context) {
        if (null == mDatabaseManager) {
            synchronized (DBManager.class) {
                if (null == mDatabaseManager) {
                    mDatabaseManager = new DBManager(context);
                }
            }
        }
        return mDatabaseManager;
    }

    /**
     * 插入单个下载model
     *
     * @param downloadInfo
     */
    public void addDownloadInfo(DownloadInfo downloadInfo) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        ContentValues values = new ContentValues();
        values.put(AppConstants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
        values.put(AppConstants.DB_COLUMN_TARGET_ID, downloadInfo.getId());
        values.put(AppConstants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
        values.put(AppConstants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
        values.put(AppConstants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
        values.put(AppConstants.DB_COLUMN_TARGET_PATH, downloadInfo.getPath());
        long rowId = db.insert(DBHelper.DB_NAME, null, values);
        if (rowId == -1) {
            LogUtils.i(TAG, "插入成功");
        }
        db.endTransaction();
    }

    /**
     * 插入一个集合
     * @param downloadInfos
     */
    public void addAll(List<DownloadInfo> downloadInfos) {
        downloadInfos.forEach(downloadInfo -> {
            addDownloadInfo(downloadInfo);
        });
    }


}