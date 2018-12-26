package com.sqlist.simplenote.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.sqlist.simplenote.dateSource.DataSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by asus on 2018/1/9.
 */

public class NoteDao {
    private static final String TAG = "-SimpleNote-";
    private DataSource dataSource;

    public NoteDao(Context context)
    {
        dataSource = new DataSource(context, "simpleNote.db3", null, 1);
    }

    public SQLiteDatabase getSQLiteDatabase()
    {
        return dataSource.getReadableDatabase();
    }

    public List<Map<String, Object>> getAll()
    {
        Cursor cursor = getSQLiteDatabase().query("simple_note",
                new String[]{"id", "content", "time"},
                null, null, null, null,
                "time DESC", null);

        List<Map<String, Object>> mapList = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Map<String, Object> params = new HashMap<>();
            params.put("id", cursor.getInt(0));
            params.put("content", cursor.getString(1));
            params.put("time", cursor.getString(2));
            Log.d(TAG, "id: " + params.get("id") + ", content: " + params.get("content")
                 + "time: " + params.get("time"));

            mapList.add(params);
        }

        return mapList;
    }

    public Boolean add(Map<String, Object> params)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("content", params.get("content").toString());
            values.put("time", params.get("time").toString());
            Log.d(TAG, "content: " + params.get("content").toString() + ", time: " + params.get("time").toString());

            getSQLiteDatabase().insert("simple_note", null, values);
            Log.d(TAG, "保存成功");
            return true;
        }
        catch (SQLiteException se)
        {
            Log.d(TAG, "保存失败");
            se.printStackTrace();
            return false;
        }
    }

    public Boolean delete(int id)
    {
        try
        {
            getSQLiteDatabase().delete("simple_note", "id = ?", new String[] {id + ""});
            Log.d(TAG, "删除成功");
            return true;
        }
        catch (SQLiteException se)
        {
            Log.d(TAG, "删除失败");
            se.printStackTrace();
            return false;
        }
    }

    public Boolean update(Map<String, Object> params)
    {
        try
        {
            ContentValues values = new ContentValues();
            values.put("content", params.get("content").toString());
            values.put("time", params.get("time").toString());
            Log.d(TAG,  "更新id: " + params.get("id") + "content: " + params.get("content").toString() + ", time: " + params.get("time").toString());

            getSQLiteDatabase().update("simple_note", values, "id = ?", new String[]{params.get("id") + ""});
            Log.d(TAG, "更新成功");
            return true;
        }
        catch (SQLiteException se)
        {
            Log.d(TAG, "更新失败");
            se.printStackTrace();
            return false;
        }
    }

    public void close()
    {
        dataSource.close();
    }
}
