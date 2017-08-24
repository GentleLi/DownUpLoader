package com.gentler.downuploader.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gentler.downuploader.config.AppConstants;
import com.gentler.downuploader.utils.LogUtils;


/**
 * Created by Jiantao on 2017/6/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DB_NAME = "download.db";
    public static final String TABLE_NAME = "download_info";
    public static int DB_VERSION = 2;
    static final String DATABASE_CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            AppConstants.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AppConstants.DB_COLUMN_TARGET_ID + " VARCHAR NOT NULL, " +
            AppConstants.DB_COLUMN_TARGET_NAME + " VARCHAR NOT NULL UNIQUE, " +
            AppConstants.DB_COLUMN_DOWNLOAD_URL + " VARCHAR, " +
            AppConstants.DB_COLUMN_TARGET_SIZE + " INTEGER, " +
            AppConstants.DB_COLUMN_CURR_POS + " INTEGER, " +
            AppConstants.DB_COLUMN_TARGET_DIR + " CHAR " +
            ")";
    static final String DATABASE_UPGRADE_STATEMENT = "DROP TABLE IF EXISTS " + DB_NAME;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtils.e(TAG, "OnCreate");
        db.execSQL(DATABASE_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogUtils.e(TAG, "onUpgrade");
        db.execSQL(DATABASE_UPGRADE_STATEMENT);
    }


}
