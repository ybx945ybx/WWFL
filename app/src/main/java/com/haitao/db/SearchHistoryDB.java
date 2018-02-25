package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class SearchHistoryDB {
    private static final String TAG = "SearchHistoryDB";

    public static void add(String str) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.SEARCH_TABLE, null, DBConstant.SEARCH_NAME + "=?", new String[]{str}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.SEARCH_NAME, cursor.getString(cursor.getColumnIndex(DBConstant.SEARCH_NAME)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.SEARCH_TABLE, cv, DBConstant.SEARCH_NAME + "=?", new String[]{str});
            Logger.d("update");
        } else {
            cv.put(DBConstant.SEARCH_NAME, str);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.SEARCH_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.SEARCH_TABLE, null, cv);
    }

    public static ArrayList<String> getList() {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.SEARCH_TABLE, null,
                null, null, null, null,
                DBConstant.UPDATE_TIME + " desc", "5");
        ArrayList<String> historyList = new ArrayList<String>();
        while (cursor.moveToNext()) {
            historyList.add(cursor.getString(cursor.getColumnIndex(DBConstant.SEARCH_NAME)));
        }
        if (null != cursor) {
            cursor.close();
        }
        return historyList;
    }

    public static void clear() {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.SEARCH_TABLE, null, null);
    }
}
