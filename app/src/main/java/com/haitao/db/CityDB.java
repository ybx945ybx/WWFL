package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.model.CityObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class CityDB {
    private static final String TAG = "CityDB";

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public static void add(CityObject obj) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.CITY_TABLE, null, DBConstant.CITY_ID + "=?", new String[]{obj.city_id}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.CITY_ID, cursor.getString(cursor.getColumnIndex(DBConstant.CITY_ID)));
            cv.put(DBConstant.CITY_NAME, cursor.getString(cursor.getColumnIndex(DBConstant.CITY_NAME)));
            cv.put(DBConstant.CITY_PID, cursor.getString(cursor.getColumnIndex(DBConstant.CITY_PID)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.CITY_TABLE, cv, DBConstant.PROVINCE_ID + "=?", new String[]{obj.city_id});
            Logger.d("update");
        } else {
            cv.put(DBConstant.CITY_ID, obj.city_id);
            cv.put(DBConstant.CITY_NAME, obj.name);
            cv.put(DBConstant.CITY_PID, obj.upid);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CITY_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<CityObject> list) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.beginTransaction();
        for (CityObject obj : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.CITY_ID, obj.city_id);
            cv.put(DBConstant.CITY_NAME, obj.name);
            cv.put(DBConstant.CITY_PID, obj.upid);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CITY_TABLE, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.CITY_TABLE, null, cv);
    }

    public static ArrayList<CityObject> getList(String pid) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.CITY_TABLE, null,
                DBConstant.CITY_PID + "=?", new String[]{pid}, null, null, null);
        ArrayList<CityObject> list = new ArrayList<CityObject>();
        while (cursor.moveToNext()) {
            CityObject obj = new CityObject();
            obj.city_id = cursor.getString(cursor.getColumnIndex(DBConstant.CITY_ID));
            obj.name = cursor.getString(cursor.getColumnIndex(DBConstant.CITY_NAME));
            obj.upid = cursor.getString(cursor.getColumnIndex(DBConstant.CITY_PID));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    public static void clear() {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.CITY_TABLE, null, null);
    }
}
