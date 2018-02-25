package com.haitao.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.haitao.common.Constant.CategoryConstant;
import com.haitao.model.TagObject;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class ThreadCategoryDB {
    private static final String TAG = "ThreadCategoryDB";

    /**
     * 添加一条数据
     *
     * @param obj
     */
    public static void add(TagObject obj, String type) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor        cursor = db.query(DBConstant.CATEGORY_TABLE, null, DBConstant.CATEGORY_ID + "=? and " + DBConstant.CATEGORY_TYPE + "=?", new String[]{obj.id, type}, null, null, null);
        ContentValues cv     = new ContentValues();
        if (cursor.moveToNext()) {
            cv.put(DBConstant.ID, cursor.getInt(cursor.getColumnIndex(DBConstant.ID)));
            cv.put(DBConstant.CATEGORY_ID, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_ID)));
            cv.put(DBConstant.CATEGORY_TEXT, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TEXT)));
            cv.put(DBConstant.CATEGORY_BRANCH, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_BRANCH)));
            cv.put(DBConstant.CATEGORY_TYPE, cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TYPE)));
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.update(DBConstant.CATEGORY_TABLE, cv, DBConstant.CATEGORY_ID + "=? and " + DBConstant.CATEGORY_TYPE + "=?", new String[]{obj.id, type});
            Logger.d("update");
        } else {
            cv.put(DBConstant.CATEGORY_ID, obj.id);
            cv.put(DBConstant.CATEGORY_TEXT, obj.name);
            cv.put(DBConstant.CATEGORY_BRANCH, obj.fid);
            cv.put(DBConstant.CATEGORY_TYPE, type);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CATEGORY_TABLE, null, cv);
            Logger.d("insert");
        }
    }

    /**
     * 批量添加
     *
     * @param list
     */
    public static void add(ArrayList<TagObject> list, String type) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.beginTransaction();
        for (TagObject obj : list) {
            ContentValues cv = new ContentValues();
            cv.put(DBConstant.CATEGORY_ID, obj.id);
            cv.put(DBConstant.CATEGORY_TEXT, obj.name);
            cv.put(DBConstant.CATEGORY_BRANCH, obj.fid);
            cv.put(DBConstant.CATEGORY_TYPE, type);
            cv.put(DBConstant.UPDATE_TIME, System.currentTimeMillis());
            db.insert(DBConstant.CATEGORY_TABLE, null, cv);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public static void add(ContentValues cv) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        db.insert(DBConstant.CATEGORY_TABLE, null, cv);
    }

    public static ArrayList<TagObject> getList(String fid, int type) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.CATEGORY_TABLE, null,
                DBConstant.CATEGORY_TYPE + "=? and " + DBConstant.CATEGORY_BRANCH + "=?", new String[]{String.valueOf(type), fid}, null, null, null);
        Logger.d(DBConstant.CATEGORY_TYPE + "=? and " + DBConstant.CATEGORY_BRANCH + "=?" + CategoryConstant.THREAD_CATEGORY + "//" + fid);
        ArrayList<TagObject> list = new ArrayList<TagObject>();
        while (cursor.moveToNext()) {
            TagObject obj = new TagObject();
            obj.id = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_ID));
            obj.text = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_TEXT));
            obj.fid = cursor.getString(cursor.getColumnIndex(DBConstant.CATEGORY_BRANCH));
            list.add(obj);
        }
        if (null != cursor) {
            cursor.close();
        }
        return list;
    }

    /**
     * 判断是否为空
     *
     * @return
     */
    public static boolean isEmpty(int type) {
        SQLiteDatabase db = SQLiteUtil.getInstance()
                .getSqLiteDatabase();
        Cursor cursor = db.query(DBConstant.CATEGORY_TABLE, null,
                DBConstant.CATEGORY_TYPE + "=?", new String[]{String.valueOf(type)}, null, null, null);
        boolean isEmpty = true;
        if (cursor.moveToNext()) {
            isEmpty = false;
        }
        return isEmpty;
    }

    public static void clear(int type) {
        SQLiteDatabase db = SQLiteUtil.getInstance().getSqLiteDatabase();
        db.delete(DBConstant.CATEGORY_TABLE, DBConstant.CATEGORY_TYPE + "=?", new String[]{String.valueOf(type)});
    }
}
