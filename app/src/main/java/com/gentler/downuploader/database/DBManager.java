package com.gentler.downuploader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.provider.SyncStateContract;

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
        try{
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
                LogUtils.i(TAG, "插入失败");
            }else{
                LogUtils.i(TAG, "插入成功");
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
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
        LogUtils.d(TAG,"query");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            Cursor cursor = db.query(DBHelper.DB_NAME, null, AppConstants.DB_COLUMN_ID +" = ?", new String[]{id}, null, null, null);
            if (null != cursor) {
                while (cursor.moveToNext()) {
                    int count = cursor.getColumnCount();
                    LogUtils.e(TAG, "count==" + count);
                    LogUtils.e(TAG, "cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID):" + cursor.getColumnIndex(AppConstants.DB_COLUMN_TARGET_ID));
                    for (int i = 0; i < count; i++) {
                        LogUtils.e(TAG, "cursor.getColumnName(" + i + "):" + cursor.getColumnName(i));
                    }
                }
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }
    }

    /**
     * 根据id删除数据
     * @param id
     */
    public void remove(String id){
        LogUtils.d(TAG,"remove");
        SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
        try{
            db.beginTransaction();
            int results=db.delete(DBHelper.DB_NAME,AppConstants.DB_COLUMN_ID+" = ?",new String[]{id});
            LogUtils.d(TAG,"results:"+results);
            if (results==0){//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                LogUtils.d(TAG,"删除失败");
            }else{//删除了多少行
                LogUtils.d(TAG,"删除成功");
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }

    /**
     * 更新数据
     * @param downloadInfo
     */
    public void update(DownloadInfo downloadInfo){
        LogUtils.d(TAG,"update");
        SQLiteDatabase db=mDatabaseHelper.getWritableDatabase();
        try{
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(AppConstants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
            values.put(AppConstants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
            values.put(AppConstants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
            values.put(AppConstants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
            values.put(AppConstants.DB_COLUMN_TARGET_PATH, downloadInfo.getPath());
            int results=db.update(DBHelper.DB_NAME,values,AppConstants.DB_COLUMN_TARGET_ID+"= ?",new String[]{downloadInfo.getId()});
            LogUtils.d(TAG,"results:"+results);
            if (results==0){//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                LogUtils.d(TAG,"更新失败");
            }else{//删除了多少行
                LogUtils.d(TAG,"更新成功");
            }
        }catch(SQLiteException e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
    }



}
