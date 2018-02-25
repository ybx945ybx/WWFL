package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.model.ProvinceObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class ProvinceDB {
    private static final String TAG = "ProvinceDB";

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public static void add(ProvinceObject obj) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.PROVINCE_TABLE, null, DBConstant.PROVINCE_ID + "=?", new String[]{obj.id}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.PROVINCE_ID, cursor.getString(cursor.getColumnIndex(DBConstant.PROVINCE_ID)));
            cv.put(DBConstant.PROVINCE_NAME, cursor.getString(cursor.getColumnIndex(DBConstant.PROVINCE_NAME)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.PROVINCE_TABLE, cv, DBConstant.PROVINCE_ID + "=?", new String[]{obj.id});
            Logger.d("update");
        } else {
            cv.put(DBConstant.PROVINCE_ID, obj.id);
            cv.put(DBConstant.PROVINCE_NAME, obj.province);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.PROVINCE_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<ProvinceObject> list) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.beginTransaction();
        for (ProvinceObject obj : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.PROVINCE_ID, obj.id);
            cv.put(DBConstant.PROVINCE_NAME, obj.province);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.PROVINCE_TABLE, null, cv);
            Logger.d("insert " + cv.toString());
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.PROVINCE_TABLE, null, cv);
    }

    public static ArrayList<ProvinceObject> getList() {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.PROVINCE_TABLE, null,
                null, null, null, null, null);
        ArrayList<ProvinceObject> list = new ArrayList<ProvinceObject>();
        while (cursor.moveToNext()) {
            ProvinceObject obj = new ProvinceObject();
            obj.id = cursor.getString(cursor.getColumnIndex(DBConstant.PROVINCE_ID));
            obj.province = cursor.getString(cursor.getColumnIndex(DBConstant.PROVINCE_NAME));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    public static void clear() {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.PROVINCE_TABLE, null, null);
    }
}
