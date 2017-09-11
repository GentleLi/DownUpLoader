package com.gentler.downloaderlib.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gentler.downloaderlib.config.Constants;


/**
 * Created by Jiantao on 2017/6/14.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = DBHelper.class.getSimpleName();
    public static final String DB_NAME = "download.db";
    public static final String TABLE_NAME = "download_info";
    public static int DB_VERSION = 1;
    static final String DATABASE_CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " +
            TABLE_NAME + "(" +
            Constants.DB_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Constants.DB_COLUMN_TARGET_ID + " VARCHAR NOT NULL, " +
            Constants.DB_COLUMN_TARGET_NAME + " VARCHAR NOT NULL UNIQUE, " +
            Constants.DB_COLUMN_DOWNLOAD_URL + " VARCHAR, " +
            Constants.DB_COLUMN_TARGET_SIZE + " INTEGER, " +
            Constants.DB_COLUMN_CURR_POS + " INTEGER, " +
            Constants.DB_COLUMN_TARGET_DIR + " CHAR " +
            ")";
    static final String DATABASE_UPGRADE_STATEMENT = "DROP TABLE IF EXISTS " + DB_NAME;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "OnCreate");
        db.execSQL(DATABASE_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(TAG, "onUpgrade");
        db.execSQL(DATABASE_UPGRADE_STATEMENT);
        db.execSQL(DATABASE_CREATE_STATEMENT);
    }


}
