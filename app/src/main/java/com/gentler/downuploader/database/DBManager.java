package com.gentler.downuploader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.gentler.downuploader.config.AppConstants;
import com.gentler.downuploader.model.DownloadInfo;
import com.gentler.downuploader.utils.LogUtils;

import java.util.List;

/**
 * Created by jiantao on 2017/6/14.
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
        db.beginTransaction();//开启事务
        try {
            ContentValues values = new ContentValues();
            values.put(AppConstants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
            values.put(AppConstants.DB_COLUMN_TARGET_ID, downloadInfo.getId());
            values.put(AppConstants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
            values.put(AppConstants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
            values.put(AppConstants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
            values.put(AppConstants.DB_COLUMN_TARGET_PATH, downloadInfo.getPath());
            long rowId = db.insert(DBHelper.TABLE_NAME, AppConstants.DB_COLUMN_TARGET_PATH, values);
            Log.d(TAG, "rowId:" + rowId);
            if (rowId == -1) {
                LogUtils.i(TAG, "插入失败");
            } else {
                LogUtils.i(TAG, "插入成功");
                db.setTransactionSuccessful();//设置事务的标志为True
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void insert(DownloadInfo info) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.execSQL("INSERT INTO " + DBHelper.TABLE_NAME + "(" +
                        AppConstants.DB_COLUMN_TARGET_NAME + ", " +
                        AppConstants.DB_COLUMN_TARGET_ID + ", " +
                        AppConstants.DB_COLUMN_CURR_POS + ", " +
                        AppConstants.DB_COLUMN_DOWNLOAD_URL + ", " +
                        AppConstants.DB_COLUMN_TARGET_SIZE + ", " +
                        AppConstants.DB_COLUMN_TARGET_PATH + ") values (?,?,?,?,?,?)",
                new Object[]{info.getName(), info.getId(), info.getCurrPos(), info.getDownloadUrl(),
                        info.getSize(), info.getPath()});
        db.close();
    }


    /**
     * 插入一个集合
     *
     * @param downloadInfos
     */
    public void addAll(List<DownloadInfo> downloadInfos) {
        downloadInfos.forEach(downloadInfo -> {
            addDownloadInfo(downloadInfo);
        });
    }

    /**
     * 查询下载信息
     *
     * @param id 关键字 id 在此实际为 下载资源的resourseId
     */
    public void query(String id) {
        LogUtils.d(TAG, "query");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(DBHelper.TABLE_NAME, null, AppConstants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id}, null, null, null);
            if (null != cursor) {
                int count = cursor.getColumnCount();
                LogUtils.e(TAG, "count==" + count);
                while (cursor.moveToNext()) {
                    LogUtils.e(TAG, "cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID):" + cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID));
                    for (int i = 0; i < count; i++) {
                        LogUtils.e(TAG, "cursor.getColumnName(" + i + "):" + cursor.getColumnName(i));
                    }
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public DownloadInfo find(String id) {
        DownloadInfo downloadInfo = null;
        LogUtils.d(TAG, "find");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            Cursor cursor = db.query(DBHelper.TABLE_NAME, null, AppConstants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToNext()) {//查询出第一个
                    downloadInfo = new DownloadInfo();
                    int count = cursor.getColumnCount();
                    LogUtils.e(TAG, "count==" + count);
                    LogUtils.e(TAG, "cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID):" + cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID));
                    for (int i = 0; i < count; i++) {
                        LogUtils.e(TAG, "cursor.getColumnName(" + i + "):" + cursor.getColumnName(i));
                        switch (cursor.getColumnName(i)) {
                            case AppConstants.DB_COLUMN_TARGET_ID:
                                String target_id = cursor.getString(cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID));
                                downloadInfo.setId(target_id);
                                break;
                            case AppConstants.DB_COLUMN_CURR_POS:
                                long curr_pos = cursor.getLong(cursor.getColumnIndex(AppConstants.DB_COLUMN_CURR_POS));
                                downloadInfo.setCurrPos(curr_pos);
                                break;
                            case AppConstants.DB_COLUMN_DOWNLOAD_URL:
                                String url = cursor.getString(cursor.getColumnIndex(AppConstants.DB_COLUMN_DOWNLOAD_URL));
                                downloadInfo.setDownloadUrl(url);
                                break;
                            case AppConstants.DB_COLUMN_TARGET_NAME:
                                String name = cursor.getString(cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_NAME));
                                downloadInfo.setName(name);
                                break;
                            case AppConstants.DB_COLUMN_TARGET_PATH:
                                String path = cursor.getString(cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_PATH));
                                downloadInfo.setPath(path);
                                break;
                            case AppConstants.DB_COLUMN_TARGET_SIZE:
                                long size = cursor.getLong(cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_SIZE));
                                downloadInfo.setSize(size);
                                break;
                        }
                    }
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
        return downloadInfo;
    }


    /**
     * 根据id删除数据
     *
     * @param id
     */
    public void remove(String id) {
        LogUtils.d(TAG, "remove");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            int results = db.delete(DBHelper.TABLE_NAME, AppConstants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id});
            LogUtils.d(TAG, "results:" + results);
            if (results == 0) {//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                LogUtils.d(TAG, "删除失败");
            } else {//删除了多少行
                LogUtils.d(TAG, "删除成功");
                db.setTransactionSuccessful();//设置事务的标志为true，表示这次操作成功
                //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 更新数据
     *
     * @param downloadInfo
     */
    public void update(DownloadInfo downloadInfo) {
        LogUtils.d(TAG, "update");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(AppConstants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
            values.put(AppConstants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
            values.put(AppConstants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
            values.put(AppConstants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
            values.put(AppConstants.DB_COLUMN_TARGET_PATH, downloadInfo.getPath());
            int results = db.update(DBHelper.TABLE_NAME, values, AppConstants.DB_COLUMN_TARGET_ID + "= ?", new String[]{downloadInfo.getId()});
            LogUtils.d(TAG, "results:" + results);
            if (results == 0) {//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                LogUtils.d(TAG, "更新失败");
            } else {//删除了多少行
                LogUtils.d(TAG, "更新成功");
                db.setTransactionSuccessful();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }


}
