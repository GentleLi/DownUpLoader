package com.gentler.downloaderlib.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import com.gentler.downloaderlib.config.Constants;
import com.gentler.downloaderlib.model.DownloadInfo;


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
            values.put(Constants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
            values.put(Constants.DB_COLUMN_TARGET_ID, downloadInfo.getId());
            values.put(Constants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
            values.put(Constants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
            values.put(Constants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
            values.put(Constants.DB_COLUMN_TARGET_DIR, downloadInfo.getDir());
            long rowId = db.insert(DBHelper.TABLE_NAME, null, values);
            Log.d(TAG, "rowId:" + rowId);
            if (rowId == -1) {
                Log.i(TAG, "插入失败");
            } else {
                Log.i(TAG, "插入成功");
                db.setTransactionSuccessful();//设置事务的标志为True
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (null!=db){
                db.endTransaction();
                db.close();
            }
        }
    }

    public void insert(DownloadInfo info) {
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.execSQL("INSERT INTO " + DBHelper.TABLE_NAME + "(" +
                        Constants.DB_COLUMN_TARGET_NAME + ", " +
                        Constants.DB_COLUMN_TARGET_ID + ", " +
                        Constants.DB_COLUMN_CURR_POS + ", " +
                        Constants.DB_COLUMN_DOWNLOAD_URL + ", " +
                        Constants.DB_COLUMN_TARGET_SIZE + ", " +
                        Constants.DB_COLUMN_TARGET_DIR + ") values (?,?,?,?,?,?)",
                new Object[]{info.getName(), info.getId(), info.getCurrPos(), info.getDownloadUrl(),
                        info.getSize(), info.getDir()});
        db.close();
    }


    /**
     * 插入一个集合
     *
     * @param downloadInfos
     */
//    public void addAll(List<DownloadInfo> downloadInfos) {
//        downloadInfos.forEach(downloadInfo -> {
//            addDownloadInfo(downloadInfo);
//        });
//    }

    /**
     * 查询下载信息
     *
     * @param id 关键字 id 在此实际为 下载资源的resourseId
     */
    public void query(String id) {
        Log.d(TAG, "query");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(DBHelper.TABLE_NAME, null, Constants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id}, null, null, null);
            if (null != cursor) {
                int count = cursor.getColumnCount();
                Log.e(TAG, "count==" + count);
                while (cursor.moveToNext()) {
                    Log.e(TAG, "cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_ID):" + cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_ID));
                    for (int i = 0; i < count; i++) {
                        Log.e(TAG, "cursor.getColumnName(" + i + "):" + cursor.getColumnName(i));
                    }
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (null!=db){
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 数据库中是否已经存在此数据
     *
     * @param downloadInfo
     * @return
     */
    public boolean has(DownloadInfo downloadInfo) {
        if (null == downloadInfo) {
            throw new NullPointerException("Oops! downloadInfo is null");
        }
        return has(downloadInfo.getId());
    }

    /**
     * 是否已经存在此数据
     *
     * @param id
     * @return
     */
    public boolean has(String id) {
        if (TextUtils.isEmpty(id)) {
            return false;
        }
        Log.d(TAG, "find");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, Constants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id}, null, null, null);
        if (null != cursor && cursor.moveToNext()) {
            return true;
        }
        return false;
    }

    public DownloadInfo find(String id) {
        DownloadInfo downloadInfo = null;
        Log.d(TAG, "find");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            Cursor cursor = db.query(DBHelper.TABLE_NAME, null, Constants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToNext()) {//查询出第一个
                    DownloadInfo.Builder builder=new DownloadInfo.Builder();
                    int count = cursor.getColumnCount();
                    Log.e(TAG, "count==" + count);
                    Log.e(TAG, "cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_ID):" + cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_ID));
                    for (int i = 0; i < count; i++) {
                        Log.e(TAG, "cursor.getColumnName(" + i + "):" + cursor.getColumnName(i));
                        switch (cursor.getColumnName(i)) {
                            case Constants.DB_COLUMN_TARGET_ID:
                                String target_id = cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_ID));
                                builder.id(target_id);
                                break;
                            case Constants.DB_COLUMN_CURR_POS:
                                long curr_pos = cursor.getLong(cursor.getColumnIndex(Constants.DB_COLUMN_CURR_POS));
                                builder.currPos(curr_pos);
                                break;
                            case Constants.DB_COLUMN_DOWNLOAD_URL:
                                String url = cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_DOWNLOAD_URL));
                                builder.downloadUrl(url);
                                break;
                            case Constants.DB_COLUMN_TARGET_NAME:
                                String name = cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_NAME));
                                builder.name(name);
                                break;
                            case Constants.DB_COLUMN_TARGET_DIR:
                                String path = cursor.getString(cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_DIR));
                                builder.dir(path);
                                break;
                            case Constants.DB_COLUMN_TARGET_SIZE:
                                long size = cursor.getLong(cursor.getColumnIndex(Constants.DB_COLUMN_TARGET_SIZE));
                                builder.size(size);
                                break;
                        }
                    }
                    downloadInfo=builder.build();
                }
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (null!=db){
                db.close();
            }
        }
        return downloadInfo;
    }


    /**
     * 根据id删除数据
     *
     * @param id
     */
    public void remove(String id) {
        Log.d(TAG, "remove");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            int results = db.delete(DBHelper.TABLE_NAME, Constants.DB_COLUMN_TARGET_ID + " = ?", new String[]{id});
            Log.d(TAG, "results:" + results);
            if (results == 0) {//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                Log.d(TAG, "删除失败");
            } else {//删除了多少行
                Log.d(TAG, "删除成功");
                db.setTransactionSuccessful();//设置事务的标志为true，表示这次操作成功
                //事务的提交或回滚是由事务的标志决定的,如果事务的标志为True，事务就会提交，否侧回滚,默认情况下事务的标志为False
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
        } finally {
            if (null!=db){
                db.endTransaction();
                db.close();
            }
        }
    }

    /**
     * 清空表
     */
    public void clearTable() {
        Log.d(TAG, "remove");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            int results = db.delete(DBHelper.TABLE_NAME, null, null);
            if (results == 0) {//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                Log.d(TAG, "删除失败");
            } else {//删除了多少行
                Log.d(TAG, "删除成功");
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
        Log.d(TAG, "update");
        SQLiteDatabase db = mDatabaseHelper.getWritableDatabase();
        try {
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Constants.DB_COLUMN_TARGET_NAME, downloadInfo.getName());
            values.put(Constants.DB_COLUMN_CURR_POS, downloadInfo.getCurrPos());
            values.put(Constants.DB_COLUMN_DOWNLOAD_URL, downloadInfo.getDownloadUrl());
            values.put(Constants.DB_COLUMN_TARGET_SIZE, downloadInfo.getSize());
            values.put(Constants.DB_COLUMN_TARGET_DIR, downloadInfo.getDir());
            int results = db.update(DBHelper.TABLE_NAME, values, Constants.DB_COLUMN_TARGET_ID + "= ?", new String[]{downloadInfo.getId()});
            Log.d(TAG, "results:" + results);
            if (results == 0) {//说明没有删除数据（数据库中不存在此数据或者 删除过程出现问题而失败）
                Log.d(TAG, "更新失败");
            } else {//删除了多少行
                Log.d(TAG, "更新成功");
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
