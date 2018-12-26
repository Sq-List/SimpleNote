package com.sqlist.simplenote.dateSource;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by asus on 2018/1/9.
 */

public class DataSource extends SQLiteOpenHelper {

    private static final String TAG = "-SimpleNote-";
    private final static String CREATE_TABLE_SQL =
            "CREATE TABLE `simple_note` (" +
            "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
            "`content`  text NULL ," +
            "`time`  varchar(255) NOT NULL" +
            ");";

    public DataSource(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataSource(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "创建数据库simple_note");
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
